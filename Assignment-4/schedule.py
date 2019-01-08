import sqlite3
from persistence import *


def findCourse(class_id):
    res = repo.courses.find(class_id=class_id)
    if len(res) == 0:
        return 0
    return repo.courses.find(class_id=class_id)[0]


def assignedCourse(course_id):
    students_in_course_number = repo.courses.find(id=course_id)[0].number_of_students
    graduate = repo.courses.find(id=course_id)[0].student.decode('UTF-8')
    next_student_number = repo.students.find(grade=graduate)[0].count - students_in_course_number
    repo.students.update({"count": next_student_number}, {'grade': graduate})


def updateCourseFields(co, class_id):
    repo.classrooms.update({'current_course_id': co.id, 'current_course_time_left': co.course_length}, {'id': class_id})


def decrease_time_left(cl):
    time_left = cl.current_course_time_left
    if time_left > 0:
        repo.classrooms.update({'current_course_time_left': time_left - 1}, {'id': cl.id})
        cl.current_course_time_left = time_left - 1


def remove_course(course_id, class_id):
    repo.courses.delete(id=course_id)
    repo.classrooms.update({'current_course_id': 0}, {'id': class_id})


repo = Repository()

def main():
    students = repo.students.find_all()
    courses = repo.courses.find_all()
    classrooms = repo.classrooms.find_all()

    c = 0
    while len(courses) != 0:
        for classroom in classrooms:
            course = findCourse(classroom.id)
            if course == 0:
                continue

            if classroom.current_course_time_left == 0 and classroom.current_course_id == 0:
                assignedCourse(course.id)
                updateCourseFields(course, classroom.id)
                print("({}) {}: {} is schedule to start".format(c, classroom.location.decode('UTF-8'),
                                                                course.course_name.decode('UTF-8')))
            elif classroom.current_course_time_left > 0:
                if classroom.current_course_time_left > 1:
                    print("({}) {}: occupied by {}".format(c, classroom.location.decode('UTF-8'),
                                                           course.course_name.decode('UTF-8')))
                decrease_time_left(classroom)
            if classroom.current_course_time_left == 0 and classroom.current_course_id != 0:
                print("({}) {}: {} is done".format(c, classroom.location.decode('UTF-8'),
                                                   course.course_name.decode('UTF-8')))
                remove_course(course.id, classroom.id)
                course = findCourse(classroom.id)
                if course != 0:
                    assignedCourse(course.id)
                    updateCourseFields(course, classroom.id)
                    print("({}) {}: {} is schedule to start".format(c, classroom.location.decode('UTF-8'),
                                                                    course.course_name.decode('UTF-8')))

        students = repo.students.find_all()
        courses = repo.courses.find_all()
        classrooms = repo.classrooms.find_all()
        c = c + 1
        repo.print_db()


if __name__ == "__main__":
    main()


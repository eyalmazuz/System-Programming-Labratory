import sqlite3
from persistence import *

def checkIfAviable(classroomId):
    return repo.classrooms.find({'id':classroomId}).current_course_time_left


def assignedCourse(courseId,classroomId,graduate):
    classrooms.update({"id":classroomId},{"current_course_id":courseId})
    studentsInCourseNumber = courses.find({'id':courseId}).number_of_students
    nextStudentNumber = students.find({'grade':graduate}).count - studentsInCourseNumber
    students.update({'grade':graduate},{"count":nextStudentNumber})




repo = Repository()
students = repo.students.find_all()
courses = repo.courses.find_all()
classrooms = repo.classrooms.find_all()

while courses.count() != 0:
    for classroom in classrooms:
        if checkIfAviable(classroom.current_course_id):
            assing course
            class.courseID = courseID
            class.timeLeft = course.timeLeft
            print((i) class.getLocation course.getname is scheduled to start)
        elif class occupied
            print((i) classroom.getlocation is occupied by course.getname)
            class.current course time left -= 1
        if current course time left == 0
            print((i) classroom.getlocation course.getname is done)
            db.remove course(course)
            assign class
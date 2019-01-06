import sqlite3


def selectTable(conn,tableName):
    return conn.cursor().execute("SELECT * FROM " + tableName +"").fetchall()


def checkIfAviable(conn, classroomId):
    # inst = Dao(Classrooms, conn)
    #obj = ins.findall()
    #for classroom in obj:
    #
    return conn.cursor().execute("SELECT current_course_time_left FROM classrooms WHERE classroomId = "+classroomId+"")


def assignedCourse(conn,courseId):


conn = sqlite3.connect('classes.db')
courses = selectTable(conn,"courses")
classrooms = selectTable(conn,"classrooms")

while courses is not empty:
    for classroom in classrooms:
        if checkIfAviable(conn,classroomId=)
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
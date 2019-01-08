import atexit
import sqlite3
import sys
import os
from dbtools import Dao

class Course:

    def __init__(self, id, course_name, student, number_of_students, class_id, course_length):
        self.id = id
        self.course_name = course_name
        self.student = student
        self.number_of_students = number_of_students
        self.class_id = class_id
        self.course_length = course_length

class Classroom:

    def __init__(self, id, location, current_course_id, current_course_time_left):
        self.id = id
        self.location = location
        self.current_course_id = current_course_id
        self.current_course_time_left = current_course_time_left

class Student:

    def __init__(self, grade, count):
        self.grade = grade
        self.count = count




def print_table(list_of_tuples):
    for item in list_of_tuples:
        print(tuple([item.decode('utf-8') if type(item)==bytes else item for item in item.__dict__.values()]))
    pass


class Repository(object):
    def __init__(self):
        self._conn = sqlite3.connect('classes.db')
        self._conn.text_factory = bytes
        self.students = Dao(Student, self._conn)
        self.classrooms = Dao(Classroom, self._conn)
        self.courses = Dao(Course, self._conn)

    def _close(self):
        self._conn.commit()
        self._conn.close()

    # see code in previous version...

    def create_tables(self):
        self._conn.executescript("""
        CREATE TABLE courses(
            id INTEGER PRIMARY KEY,
            course_name TEXT NOT NULL,
            student TEXT NOT NULL,
            number_of_students INTEGER NOT NULL,
            class_id INTEGER REFERENCES classrooms(id),
            course_length INTEGER NOT NULL);

        CREATE TABLE students(
            grade TEXT PRIMARY KEY,
            count INTEGER NOT NULL);

        CREATE TABLE classrooms(
            id INTEGER PRIMARY KEY,
            location TEXT NOT NULL,
            current_course_id INTEGER NOT NULL,
            current_course_time_left INTEGER NOT NULL); 
        """)

    def print_db(self):
        print('courses')
        print_table(self.courses.find_all())
        print('classrooms')
        print_table(self.classrooms.find_all())
        print('students')
        print_table(self.students.find_all())

# singleton


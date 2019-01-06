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


def print_db(conn): # TODO FIX this bullshit
    query_data = conn.cursor()
    query_data.execute("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name ASC;")
    tuples = query_data.fetchall()
    for table in tuples:
        name = "".join(table)
        query_data.execute("SELECT * FROM "+name+";")
        data = query_data.fetchall()
        print(name)
        for item in data:
            print(item)
    pass


def insert_into_db(lines, conn):
    with open(lines, 'r') as config:
        for line in config.readlines():
            data = line.split(', ')
            table = data.pop(0)
            if table is 'S':
                conn.execute("""INSERT INTO students(grade, count) VALUES(?,?)""", data)
            elif table is 'C':
                conn.execute("""INSERT INTO courses(id, course_name, student, number_of_students, class_id, course_length) 
                VALUES(?,?,?,?,?,?)""", data)
            elif table is 'R':
                conn.execute("""INSERT INTO classrooms(id, location, current_course_id, current_course_time_left)
                 VALUES (?,?,0,0)""", data)
    conn.commit()
    print_db(conn)


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

# singleton


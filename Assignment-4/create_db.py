from persistence import *

import os



def main(args):
    if not os.path.exists('classes.db'):
        repo = Repository()
        repo.create_tables()
        with open(args[1], 'r') as config:
            for line in config.readlines():
                data = line.split(', ')
                table = data.pop(0)
                data = [d.strip() for d in data]
                if table == 'S':
                    instance = Student(*data)
                    repo.students.insert(instance)
                elif table == 'R':
                    instance = Classroom(*data, 0, 0)
                    repo.classrooms.insert(instance)
                elif table == 'C':
                    instance = Course(*data)
                    repo.courses.insert(instance)

    atexit.register(repo._close)


if __name__ == "__main__":
    main(sys.argv)
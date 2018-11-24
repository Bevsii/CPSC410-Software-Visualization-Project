from School.student import Student
from School.teacher import Teacher
from School.session import Session

n = 105 # number of students
t = 5 # number of teachers
c = 10 # number of classes available

n_temp = 0
t_temp = 0
c_temp = 0

students = []
teachers = []
sessions = []

while (n_temp < n):
    s = Student("student "+str(n_temp+1), n_temp+1)
    students.append(s)
    n_temp += 1

while (t_temp < t):
    teach = Teacher("test ", str(t_temp+1)+" teacher", t_temp+1)
    teachers.append(teach)
    t_temp += 1

n_temp = 0
t_temp = 0
while (c_temp < c):
    sesh = Session(c_temp, "sub "+str(c_temp), str(0+c_temp)+":00", "room "+str(c_temp), 25)
    while (n_temp < len(students)):
        sesh.add_student(students[n_temp])
        n_temp += 1
    i = 0
    while (t_temp < len(teachers)):
        sesh.add_teacher(teachers[t_temp])
        t_temp += 1
    c_temp += 1






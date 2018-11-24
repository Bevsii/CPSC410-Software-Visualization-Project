from School.grade import Grade

class Student:

    def __init__(self, name, id):
        self.name = name
        self.classes = []
        self.grades = []
        self.id = id

    def setGrade(self, subject, grade):
        y = False
        for x in self.grades:
            if x.subject == subject:
                x.grade = grade
                y = True
        if y == False:
            g1 = Grade(subject, grade)
            self.grades.append(g1)

    # return the grade for the given subject or 0 if subject does not exist in students grades
    def getGrade(self, subject):
        ret_grade = 0
        for x in self.grades:
            if x.subject == subject:
                ret_grade = x.grade
        return ret_grade

    # add class to student's list of classes
    def add_session(self, session):
        if session not in self.classes:
            self.classes.append(session)
        print("session "+str(session.id)+" added to classes for student "+str(self.id))



s1 = Student("Rory Court", 1234)
s1.setGrade("Math", 90)




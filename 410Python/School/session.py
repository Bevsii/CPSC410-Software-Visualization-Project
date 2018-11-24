
class Session:

    def __init__(self, class_id, class_subject, class_time, class_room, class_size):
        self.id = class_id
        self.subject = class_subject
        self.time = class_time
        self.room = class_room
        self.size = class_size
        self.students = []
        self.teacher = False

    def add_teacher(self, teacher):
        if (not teacher):
            self.teacher = teacher
            teacher.assignToTeach(self)
            print("teacher successfully added")


    def add_student(self, student):
        if (len(self.students) < self.size) and student not in self.students:
            self.students.append(student)
            student.add_session(self)
            print("student successfully added, added student "+str(student.id)+", student list now equals "+str(len(self.students)))

        if (len(self.students) >= self.size):
            #print("student not added, class size limit reached")
            y = 0
        else:
            #print("student already in class")
            y=1



    def get_room(self):
        return self.room

    def get_id(self):
        return self.id

    def get_subject(self):
        return self.subject

    def get_time(self):
        return self.time

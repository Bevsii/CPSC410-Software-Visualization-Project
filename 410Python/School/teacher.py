from School.session import Session

class Teacher:

    def __init__(self, fname, lname, id):
        self.fName = fname
        self.lName = lname
        self.id = id
        self.classes = []

    def assignToTeach(self, session):
        already_teaching = False
        for x in self.classes:
            if x.id == session.get_id():
                #print('found a matching session: '+session.id+" matches "+x.id)
                already_teaching = True
        if not already_teaching:
            #print("no matching session found, adding session: " + str(session.id))
            self.classes.append(session)
            session.add_teacher(self)




# t1 = Teacher("test", "user", 1)
# i=1
# while i < 100:
#     s1 = Session(i, str(i), i, "Wood 2")
#     t1.assignToTeach(s1)
#     i += 1
#
# j = 0
# while j < len(t1.classes):
#     print(t1.classes[j].id)
#     j += 1




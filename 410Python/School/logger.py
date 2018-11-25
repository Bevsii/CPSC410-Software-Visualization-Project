import inspect
inspect_out_file = open("dynamic.json", "w")

def log():
    # [x][y]; x = position on stack; y: if 3, gives name
    caller = inspect.stack()[2][3]
    current = inspect.stack()[1][3]
    inspect_out_file.write("\""+caller+"\"" + " : {\n" +
                           "\""+current+"\"\n" +
                           "}" +
                           "\n")

def startlog():
    inspect_out_file.write("\"dynamic\" : [\n")


def endlog():
    inspect_out_file.write("]")

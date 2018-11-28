import json
import inspect
import os


def log(paramsDict):
    # [x][y]; x = position on stack; y: if 3, gives name
    caller = inspect.stack()[2][3]
    current = inspect.stack()[1][3]
    dynamicBlock = {
        "called": current,
        "caller": caller,
        "params": paramsDict
    }

    dynamic['dynamic'].append(dynamicBlock)


def startlog():
    try:
        open('dynamic.json', 'r')
    except IOError:
        open('dynamic.json', 'w')
    with open('dynamic.json', 'r') as dynamicJSON:
        global dynamic
        if os.stat('dynamic.json').st_size == 0 :
            dynamic = {}
            dynamic['dynamic'] = []
        else:
            dynamic = json.load(dynamicJSON)


def endlog():
    with open('dynamic.json', 'w') as dynamicJSON:
        json.dump(dynamic, dynamicJSON)


# This outputs a JSON file with the following format:
# {
#    "dynamic": [
#        { ... },
#        { ... }
#    ]
# }

# With the output, we can append the output file to our static analysis done in Java.

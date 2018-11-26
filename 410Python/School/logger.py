import json
import inspect

outfile = open('dynamic.json', 'w')


def log(paramsDict):
    # [x][y]; x = position on stack; y: if 3, gives name
    caller = inspect.stack()[2][3]
    current = inspect.stack()[1][3]
    dynamicBlock = {
        "called": current,
        "caller": caller,
        "params": paramsDict
    }

    dynamicJSON['dynamic'].append(dynamicBlock)


def startlog():
    global dynamicJSON
    dynamicJSON = {}
    dynamicJSON['dynamic'] = []


def endlog():
    json.dump(dynamicJSON, outfile)


'''
This outputs a JSON file with the following format:

{
    "dynamic": [
        { ... },
        { ... }
    ]
}

With the output, we can append the output file to our static analysis done in Java.
'''

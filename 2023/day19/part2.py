import sys
import copy

input_filepath = sys.argv[1]

lines = open(input_filepath).read().strip().split("\n")
L = []
i = 0
while len(lines[i]) > 0:
    L.append(lines[i])
    i += 1


class Ruleset:
    def __init__(self, nextStep):
        self.rules = []
        self.nextStep = nextStep


# build hashmap
workflows = {}
for line in L:
    name = line[: line.index("{")]
    rules = line[line.index("{") + 1 : line.index("}")]

    workflows[name] = rules.split(",")

acceptable_rulesets = []
move_stack = [Ruleset("in")]

while len(move_stack) > 0:
    ruleset = copy.deepcopy(move_stack.pop())

    # if the next step is "A", add to acceptable, if it's R, do nothing
    if ruleset.nextStep == "A":
        acceptable_rulesets.append(copy.deepcopy(ruleset))
        continue
    if ruleset.nextStep == "R":
        continue

    workflow = workflows[ruleset.nextStep]

    # if we are at the end, then add just one thing
    if len(workflow) == 1:
        # remove from hashmap since we don't need it anymore
        workflows.pop(ruleset.nextStep)

        # add next step to ruleset
        ruleset.nextStep = workflow[0]
        move_stack.append(ruleset)

        continue

    line = workflow[0]

    # pare down the workflow
    workflows[ruleset.nextStep] = workflows[ruleset.nextStep][1:]

    # now add both ways to the ruleset
    next_rule, next_workflow = line.split(":")

    # have to add it both ways
    satisfied_ruleset = copy.deepcopy(ruleset)
    satisfied_ruleset.rules.append(next_rule)
    satisfied_ruleset.nextStep = next_workflow
    move_stack.append(satisfied_ruleset)

    # now add the other way (not satisfied )
    if ">" in next_rule:
        next_rule = next_rule.replace(">", "<=")
    else:
        next_rule = next_rule.replace("<", ">=")

    ruleset.rules.append(next_rule)
    move_stack.append(ruleset)

# define a function to deal with an acceptable ruleset
working_parts = set()


def acceptable_part_combos(ruleset):
    vals = {
        "x": list(range(1, 4001)),
        "m": list(range(1, 4001)),
        "a": list(range(1, 4001)),
        "s": list(range(1, 4001)),
    }

    for rule in ruleset.rules:
        dict_val = rule[0]
        if "<=" in rule:
            operator = "<="
            val = int(rule[3:])
        elif ">=" in rule:
            operator = ">="
            val = int(rule[3:])
        else:
            operator = rule[1]
            val = int(rule[2:])

        if operator == ">":
            vals[dict_val] = [x for x in vals[dict_val] if x > val]
        elif operator == ">=":
            vals[dict_val] = [x for x in vals[dict_val] if x >= val]
        elif operator == "<=":
            vals[dict_val] = [x for x in vals[dict_val] if x <= val]
        else:
            vals[dict_val] = [x for x in vals[dict_val] if x < val]

    result = 1
    for val in vals.values():
        result *= len(val)
        # print(min(val), max(val))
    # print(ruleset.rules, result)
    return result


# first need to collapse down the ruleset
res = 0
for ruleset in acceptable_rulesets:
    res += acceptable_part_combos(ruleset)
print(res)

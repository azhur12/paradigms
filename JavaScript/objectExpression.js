let varMap = {"x" : 0, "y" : 1, "z" : 2}

function Const(value) {
    this.value = value;
    this.evaluate = () => this.value;
    this.toString = () => this.value.toString();
    this.prefix = () => this.value.toString();
    return this;
}

function Variable(value) {
    this.value = value
    this.evaluate = (...args) => args[varMap[this.value]];
    this.toString = () => this.value;
    this.prefix = () => this.value;
    return this;
}

function UnOperation(operand, operator, evaluateImpl) {
    this.operand = operand
    this.operator = operator
    this.evaluate = function (...args) {
        return evaluateImpl(operand.evaluate(...args))
    }
}

UnOperation.prototype = {
    "toString" : function() {
        return this.operand.toString() + " " + this.operator
    },
    "prefix" : function() {
        return "(" + this.operator + " " + this.operand.prefix() + ")"
    }
}

function Negate(operand) {
    return new UnOperation(operand, "negate" , (operand) => -1 * operand)
}

function ArcTan(operand) {
    return new UnOperation(operand, "atan", (operand) => Math.atan(operand))
}

function BinOperation(ls,rs, operator, evaluateImpl) {
    this.leftOperand = ls
    this.rightOperand = rs
    this.operator = operator
    this.evaluate = function (...args) {
        return evaluateImpl(ls.evaluate(...args), rs.evaluate(...args))
    }
}

BinOperation.prototype = {
    "toString" : function () {
        return this.leftOperand.toString() + " " +
            this.rightOperand.toString() + " " +
            this.operator;
    },
    "prefix" : function () {
        return "(" + this.operator + " " + this.leftOperand.prefix() + " " + this.rightOperand.prefix() + ")"
    }
}

function DynamicOperation(args, operator, evaluateImpl) {
    this.args = args;
    this.operator = operator;
    this.evaluate = function (...args2) {
        return evaluateImpl(...args.map(argument => argument.evaluate(...args2)))
    }
}
DynamicOperation.prototype = {
    "toString" : function () {
        return this.operator + " " +[...this.args].map((e) => e.toString()).join(" ");
    },
    "prefix" : function () {

        return "(" + this.operator + " " + this.args.map((e) => e.prefix()).join(" ") + ")";
    }
}

function Add(leftOp, rightOp) {
    return new BinOperation(leftOp, rightOp, "+", (leftOp, rightOp) => leftOp + rightOp)
}

function Subtract(leftOp, rightOp) {
    return new BinOperation(leftOp, rightOp, "-", (leftOp, rightOp) => leftOp - rightOp)
}

function Multiply(leftOp, rightOp) {
    return new BinOperation(leftOp, rightOp, "*", (leftOp, rightOp) => leftOp * rightOp)
}

function Divide(leftOp, rightOp) {
    return new BinOperation(leftOp, rightOp, "/", (leftOp, rightOp) => leftOp / rightOp)
}

function ArcTan2(leftOp, rightOp) {
    return new BinOperation(leftOp, rightOp, "atan2", (leftOp, rightOp) => Math.atan2(leftOp, rightOp))
}

function Sum(...args) {
    return new DynamicOperation(args, "sum", (...args) => args.reduce((a,b)=> (a+b), 0))
}

function Avg(...args) {
    return new DynamicOperation(args, "avg", (...args) => (args.reduce((a,b) => (a + b), 0))/args.length)
}

function createOperation(args) {
    let token = args.pop()
    if (OpMap[token] !== (args.length) && !(token in massiveOperations)) { //doesn't work with massive SUM !!!
        throw new ParsingError("unmatched count of operands and operator")
    }
    switch (token) {
        case "negate" : return new Negate(args[0]);
        case "atan" : return new ArcTan(args[0])
        case "+" : return new Add(args[1], args[0]);
        case "*" : return new Multiply(args[1], args[0]);
        case "-" : return new Subtract(args[1], args[0]);
        case "/" : return new Divide(args[1], args[0]);
        case "atan2" : return new ArcTan2(args[1], args[0]);
        case "sum" : return new Sum(...args);
        case "avg" : return new Avg(...args);
        default : return ("unknown token: " + token)
    }
}
let OpMap = { "negate" : 1, "atan" : 1, "+" : 2, "*" : 2, "-" : 2, "/" : 2, "atan2" : 2, "sum" : 3, "avg" : 3}

let massiveOperations = {"sum" : 3, "avg" : 3}

const isNumeric = n => !!Number(n) || n === "0";

function parse(str) {
    let stack = []
    let tokens = str.trim().split(/\s+/);
    for (let token of tokens) {
        if (token in varMap) {
            stack.push(new Variable(token));
        } else if (isNumeric(token)) {
            stack.push(new Const(parseInt(token)));
        } else {
            let mass = []
            let len = OpMap[token]
            for (let i = 0; i < len; i++) {
                mass[i] = stack.pop()
            }
            mass.push(token)
            stack.push(createOperation(mass));
        }
    }
    return stack[0];
}

function parsePrefix(str) {
    let tokens = tokenCreator(str)
    if (tokens[0] === "(" && tokens[tokens.length - 1] === ")") {
        tokens.pop()
        tokens.shift()
        if (isNumeric(tokens[0]) || (tokens[0] in varMap)) { // "(x)" Situation
            throw new ParsingError("Brackets around non-operation")
        }
    }
    return parsePrefixTokens(tokens)
}


function parsePrefixTokens(tokens) {
    let token = tokens[0]
    if (token in OpMap) {
        let balance = 0
        let stackOperands = []
        let index = 1
        for (let i = index; i < tokens.length; i++) {
            if (tokens[i] === "(") {
                balance++
            }
            if (tokens[i] === ")") {
                balance--
                if (balance === 0) {
                    let operand = parsePrefixTokens(tokens.slice(index + 1, i))
                    index = i + 1
                    stackOperands.push(operand)
                    continue
                }
            }
            if (balance === 0) {
                let operand = parsePrefixTokens(tokens.slice(index, i + 1))
                index++
                stackOperands.push(operand)
            }
        }
        if (OpMap[token] !== (stackOperands.length) && !(token in massiveOperations)) {
            throw new ParsingError("unmatched count of operands and operator")
        }
        if (token in massiveOperations) {

        } else {
            let firstOperand = stackOperands.pop()
            stackOperands.unshift(firstOperand)
        }
        stackOperands.push(token)
        return createOperation(stackOperands)
    } else if (token in varMap) {
        if (tokens.length > 1) {
            throw new ParsingError("Parsing Error Variable")
        }
        return new Variable(token)
    } else if (isNumeric(token)) {
        if (tokens.length > 1) {
            throw new ParsingError("Parsing Error Const")
        }
        return new Const(parseInt(token))
    } else {
        throw new UnknownTokenError("UnknownToken" + ": " + token)
    }
}

function checkingBrackets(str) {
    let balance = 0
    for (let i = 0; i < str.length; i++) {
        if (str.charAt(i) === "(") {
            balance++
        }
        if (str.charAt(i) === ")") {
            balance--
        }
        if (balance < 0) {
            throw new BracketBalanceError("Wrong bracket balance in " + i + " character")
        }
    }
    if (balance !== 0) {
        throw new BracketBalanceError("Wrong bracket balance in " + i + " character")
    }
}

function checkingSpace(isSpaceAfterToken, i) {
    if (isSpaceAfterToken === false) {
        throw new ParsingError("ParseError in " + i + " character, apparently tokens are merged")
    }
}
function checkingToken(massive, index, supposedToken, origStr, isSpaceAfterToken) {
    checkingSpace(isSpaceAfterToken, index);
    for (let i = 0; i < supposedToken.length; i++) {
        if (supposedToken.charAt(i) !== origStr.charAt(index)) {
            throw new ParsingError("ParseError in " + index + " character, unknown token")
        }
        index++
    }
    return supposedToken
}
function tokenCreator(origStr) {
    let newMassive = []
    let isSpaceAfterToken = true;
    checkingBrackets(origStr)
    for (let i = 0; i < origStr.length; i++) {
        let ch = origStr.charAt(i)
        if (/\s/.test(ch) || ch === "(" || ch === ")") {
            isSpaceAfterToken = true
            if (ch === "(" || ch === ")") {
                newMassive.push(ch)
            }
        } else if (isNumeric(ch)) {
            let number = makeNumber(ch, i, origStr, isSpaceAfterToken)
            newMassive.push(number)
            i = i + number.length - 1
            isSpaceAfterToken = false
        } else if (ch === "-" && isNumeric(origStr.charAt(i + 1))) {
            let number = makeNumber(ch, i, origStr, isSpaceAfterToken)
            newMassive.push(number)
            i = i + number.length - 1
            isSpaceAfterToken = false
        } else if (ch === "n") {
            let supposedToken = "negate";
            newMassive.push(checkingToken(newMassive, i, supposedToken, origStr,isSpaceAfterToken));
            i += supposedToken.length - 1;
            isSpaceAfterToken = false
        } else if (ch === "s") {
            let supposedToken = "sum";
            newMassive.push(checkingToken(newMassive, i, supposedToken, origStr,isSpaceAfterToken));
            i += supposedToken.length - 1;
            isSpaceAfterToken = false
        } else if (ch === "a") {
            let supposedToken = "avg";
            newMassive.push(checkingToken(newMassive, i, supposedToken, origStr,isSpaceAfterToken));
            i += supposedToken.length - 1;
            isSpaceAfterToken = false
        } else {
            checkingSpace(isSpaceAfterToken,i);
            newMassive.push(ch)
            if (ch !== "(" && ch !== ")") {
                isSpaceAfterToken = false
            }
        }
    }
    return newMassive
}

function makeNumber(ch, i, origStr, isSpaceAfterToken) {
    let resNum = []
    while (isNumeric(ch) || ch === "-") {
        resNum.push(ch)
        i++
        ch = origStr.charAt(i)
    }
    let number = resNum.join("")

    if (isSpaceAfterToken === false) {
        throw new ParsingError("ParseError in " + i + " character")
    }
    return number
}

function BracketBalanceError(message) {
    Error.call(this,message);
    this.message = message;
}
BracketBalanceError.prototype = Object.create(Error.prototype)
BracketBalanceError.prototype.name = "BracketBalanceError"

function UnknownTokenError(message) {
    Error.call(this, message);
    this.message = message;
}
UnknownTokenError.prototype = Object.create(Error.prototype)
UnknownTokenError.prototype.name = "UnknownTokenError"

function ParsingError(message) {
    Error.call(this, message);
    this.message = message;
}
ParsingError.prototype = Object.create(Error.prototype)
ParsingError.prototype.name = "ParsingError"
/*
const operation = (func) => (...args) => (x, y, z) => {
    let res = []
    for (let i = 0; i < args.length; i++) {
        res.push(args[i](x, y, z));
    }
    return func.apply(null, res);
}

 */

const operation = (func) => (...args) => (...args2) => func(...args.map(operand => operand(...args2)));
const variable = name => (...args) => (args[variables[name]]);
const cnst = a => () => a;
const one = cnst(1);
const two = cnst(2);

const negate = operation((a) => -(a));
const add = operation((a,b) => (a+b));
const subtract = operation((a,b) => (a - b));
const multiply = operation((a,b) => (a*b));
const divide = operation((a,b) => (a/b));
const sinh = operation(Math.sinh);
const cosh = operation(Math.cosh);


const variables = {"x" : 0, "y" : 1, "z" : 2};
/*
let cnst = a =>(x, y, z) => a;
let variable = a => (x, y, z) => (a === "x" ? x : (a === "y" ? y : z));
let negate = a => (x, y, z) => -1 * a(x, y, z);
let add = (a, b) => (x, y, z) => a(x, y, z) + b(x, y, z);
let subtract = (a, b) => (x, y, z) => a(x, y, z) - b(x, y, z);
let multiply = (a, b) => (x, y, z) => a(x, y, z) * b(x, y, z);
let divide = (a, b) => (x, y, z) => a(x, y, z) / b(x, y, z);

 */



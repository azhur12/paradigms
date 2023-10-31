(defn binOperation [f]
      (fn [a b]
          (fn [map]
              (f (a map) (b map))
              )))

(defn constant [a]
      (fn [map]
          (double a)))

(defn unaryOperation [f]
      (fn [a]
          (fn [map]
              (f (a map)))))

(defn variable [str]
      (fn [mapArg]
          (mapArg str)))

(defn divide [a b]
      (fn [map]
          (let [a (a map)
                b (b map)]
               (/ (double a) (double b)))))

(def add (binOperation +))
(def subtract (binOperation -))
(def multiply (binOperation *))
;;(def divide (binOperation /))
(def arcTan2 (binOperation #(Math/atan2 %1 %2)))

(def negate (unaryOperation -))
(def arcTan (unaryOperation #(Math/atan %1)))

(def operationMapFunc {'+ add '- subtract '* multiply '/ divide 'atan2 arcTan2 'negate negate 'atan arcTan})

(defn innerParserFunc [list]
      (if (list? list)
        (apply (operationMapFunc (first list)) (map innerParserFunc (rest list)))
        (if (number? list)
          (constant list)
          (variable (str list)))))

(defn parseFunction [stringExpression] (innerParserFunc (read-string (str stringExpression))))






;;******************************************************HW11
(defn proto-get
      "Returns object property respecting the prototype chain"
      ([obj key] (proto-get obj key nil))
      ([obj key default]
       (cond
         (contains? obj key) (obj key)
         (contains? obj :prototype) (proto-get (obj :prototype) key default)
         :else default)))

(defn proto-call
      "Calls object method respecting the prototype chain"
      [this key & args]
      (apply (proto-get this key) this args))

(defn field
      "Creates field"
      [key] (fn
              ([this] (proto-get this key))
              ([this def] (proto-get this key def))))

(defn method
      "Creates method"
      [key] (fn [this & args] (apply proto-call this key args)))

(def evaluate (method :evaluate))
(def toString (method :toString))
(def getOperator (field :operator))
(def getOperatorStr (field :strOp))
(def getFirstOperand (field :firstOperand))
(def getSecondOperand (field :secondOperand))

(def binOperationProto {
                        :evaluate (fn [obj args]
                                      ((getOperator obj) (evaluate (getFirstOperand obj) args) (double (evaluate (getSecondOperand obj) args))))
                        :toString (fn [obj]
                                      (str "(" (getOperatorStr obj) " " (toString (getFirstOperand obj)) " " (toString (getSecondOperand obj)) ")"))
                        })

(def unaryOperationProto {
                          :evaluate (fn [obj args]
                                        ((getOperator obj) (evaluate (getFirstOperand obj) args)))
                          :toString (fn [obj]
                                        (str "(" (getOperatorStr obj) " " (toString (getFirstOperand obj)) ")"))
                          })

(def constantProto {
                    :evaluate (fn [obj args]
                                  (getFirstOperand obj))
                    :toString (fn [obj]
                                  (str (getFirstOperand obj)))
                    })

(def variableProto {
                    :evaluate (fn [obj args]
                                  (get args (str (getFirstOperand obj))))
                    :toString (fn [obj]
                                  (str (getFirstOperand obj)))
                    })


(defn BinOperation [this firstOperand secondOperand]
      (assoc this
             :firstOperand firstOperand
             :secondOperand secondOperand))

(defn UnaryOperation [this firstOperand]
      (assoc this
             :firstOperand firstOperand))


(defn myConstructor
      "My Constructor"
      [ctor prototype operator strOp]
      (fn [& args] (apply ctor {:prototype prototype
                                :operator operator
                                :strOp strOp } args)))


(defn checkedDivide [a b]
      (if (zero? b)
        0
        (/ a b)))
(def Constant (myConstructor UnaryOperation constantProto nil nil))
(def Variable (myConstructor UnaryOperation variableProto nil nil))

(def Add (myConstructor BinOperation binOperationProto + "+"))
(def Subtract (myConstructor BinOperation binOperationProto - "-"))
(def Multiply (myConstructor BinOperation binOperationProto * "*"))
(def Divide (myConstructor BinOperation binOperationProto checkedDivide "/"))

(def Negate (myConstructor UnaryOperation unaryOperationProto - "negate"))
(def Sinh (myConstructor UnaryOperation unaryOperationProto #(Math/sinh %1) "sinh"))
(def Cosh (myConstructor UnaryOperation unaryOperationProto #(Math/cosh %1) "cosh"))

(def operationMap {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sinh Sinh 'cosh Cosh})

(defn innerParser [list]
      (if (list? list)
        (apply (operationMap (first list)) (map innerParser (rest list)))
        (if (number? list)
          (Constant list)
          (Variable (str list)))))

(defn parseObject [stringExpression] (innerParser (read-string stringExpression)))


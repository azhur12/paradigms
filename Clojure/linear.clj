(defn vecOp [f]
  (fn [a b] (mapv f a b)))

;;Vectors
(def v+ (vecOp +))
(def v- (vecOp -))
(def v* (vecOp *))
(def vd (vecOp /))

(defn scalar [a, b] (reduce + (v* a b)))

(defn vect [[a1, a2, a3], [b1, b2, b3]]
  [(- (* a2 b3) (* a3 b2))
   (- (* a3 b1) (* a1 b3))
   (- (* a1 b2) (* a2 b1))])

(defn v*s [v s] (mapv (partial * s) v))

(defn s*v [s v] (mapv (partial * s) v)) ;; helper function for m*s


;;Matrices
(def m+ (vecOp v+))
(def m- (vecOp v-))
(def m* (vecOp v*))
(def md (vecOp vd))

(defn transpose [a] (apply mapv vector a))

(defn m*s [m s] (mapv (partial s*v s) m))
(defn m*v [m v] (mapv (partial scalar v) m))
(defn m*m [m1 m2] (mapv #(mapv (partial scalar %1) (transpose m2)) m1))


(defn sOp [f] (fn [a b]
                (cond
                  (number? a) (f a b)
                  (vector? a) (mapv (sOp f) a b))))

(def s+ (sOp +))
(def s- (sOp -))
(def s* (sOp *))
(def sd (sOp /))

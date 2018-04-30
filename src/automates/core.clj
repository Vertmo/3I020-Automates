(ns automates.core
  (:gen-class)
  (:use [clojure.set]))

(def odd-aut {:init :q0
              :accept #{:q1}
              :trans {:q0 {0 #{:q0}
                           1 #{:q0 :q1}}
                      :q1 {}}})

(def mc-aut {:init :q0
             :accept #{:q0}
             :trans {:q0 {:piece #{:q1}}
                     :q1 {:piece #{:q2}
                          :cafe #{:q0}}
                     :q2 {:the #{:q0}}}})

;; Question 1.1
(defn etats [aut]
  "Ensemble des états de `aut`"
  (into #{} (keys (:trans aut))))

(etats odd-aut)

;; Question 1.2
(defn deterministe? [aut]
  "`aut` est-il deterministe ?"
  (every? (fn [t] (every? #(< (count %) 2) (vals t)))
          (vals (:trans aut))))

(deterministe? odd-aut)
(deterministe? mc-aut)

;; Question 1.3
(defn alphabet [aut]
  "Alphabet sur lequel est défini `aut`"
  (reduce (fn [a m] (clojure.set/union a
                                      (into #{} (keys (second m)))))
          #{}
          (:trans aut)))

(alphabet odd-aut)
(alphabet mc-aut)

;; Question 2.1
(defn accepte? [aut conf]
  "Y'a t-il un état acceptant de `aut` dans `conf` ?"
  (> (count (filter #(contains? (:accept aut) %) conf)) 0))

(accepte? odd-aut #{:q0, :q1})
(accepte? odd-aut #{:q0})

;; Question 2.2
(defn transition [aut conf elem]
  "Transition des états `conf` par la lettre `elem` pour l'automate `aut`"
  (reduce (fn [a m] (clojure.set/union a
                                       (reduce (fn [a2 m2] (clojure.set/union a2
                                                                              (get (second m2) elem)))
                                               #{}
                                               (filter #(= (first %) m) (:trans aut)))))
            #{}
            conf))

(transition odd-aut #{:q0} 1)
(transition odd-aut #{:q0 :q1} 0)
(transition odd-aut #{:q1} 0)
(transition mc-aut #{:q0} :piece)

;; Question 2.3
(defn reconnait? [aut s]
  "L'automate `aut` reconnait-il la séquence `s` ?"
  (accepte? aut (reduce (fn [s e] (transition aut s e))
                    #{(:init aut)}
                    s)))

(reconnait? odd-aut [1 0 1 1])
(reconnait? odd-aut [1 0 1 0])
(reconnait? mc-aut [:piece :cafe])
(reconnait? mc-aut [:piece :piece :the])
(reconnait? mc-aut [:piece :piece :cafe])
(reconnait? mc-aut [])
(reconnait? odd-aut [])

;; Question 3.1
(def mc-eps-aut {:init :q0
                 :accept #{:q0}
                 :trans {:q0 {:piece #{:q1 :q3}}
                         :q1 {nil #{:q3}
                              :piece #{:q2}}
                         :q2 {:the #{:q0}}
                         :q3 {:cafe #{:q0}}}})

;; Question 3.2
(defn reconnait-eps? [aut s]
  "L'automate-eps `aut` reconnait-il la séquence `s` ?"
  (accepte? aut (reduce (fn [s e] (clojure.set/union (transition aut s e)
                                                     (transition aut (transition aut s nil) e)
                                                     (transition aut (transition aut s e) nil)))
                    #{(:init aut)}
                    s)))

(reconnait-eps? mc-eps-aut [:piece :cafe])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(ns quil-life.dynamic
  (:use [quil core applet]))

(defonce board (atom #{[1 0] [1 1] [1 2]}))

(defonce running (atom true))

(def predefined
  {:block #{[1 1] [2 1]
            [1 2] [2 2]}
   :beehive #{[2 1] [3 1]
              [1 2] [4 2]
              [2 3] [3 3]}
   :loaf #{[2 1] [3 1]
           [1 2] [4 2]
           [2 3] [4 3]
           [3 4]}
   :boat #{[1 1] [2 1]
           [1 2] [3 2]
           [2 3]}
   :glider #{[1 2]
             [2 3]
             [3 1] [3 2] [3 3]}
   :lwss #{[5 4] [4 4] [1 1] [3 4] [2 4] [1 3] [4 1] [5 2] [5 3]}})

(defn key-typed []
  (let [k (raw-key)]
    (condp = k
      \p (do
           (swap! running not)
           (println "running:" @running))
      \c (do
           (println "clear")
           (reset! board #{}))
      (println "Don't know what to do with " k "."))))

(defn setup []
  (smooth)
  (frame-rate 10)
  (background 255))

(defn neighbours [[x y]]
  (for [dx [-1 0 1]
        dy (if (zero? dx)
             [-1 1]
             [-1 0 1])]
    [(+ dx x) (+ dy y)]))

(defn step [cells]
  (set (for [[loc n] (frequencies (mapcat neighbours cells))
             :when (or (= n 3)
                       (and (= n 2)
                            (cells loc)))]
         loc)))

(defn draw-grid []
  (stroke 100 100 200 50)
  (stroke-weight 1)
  (let [w (width)
        w (- w (mod w 10))
        h (height)
        h (- h (mod h 10))]
    (doseq [x (range 0 w 10)]
      (line x 0 x h))
    (doseq [y (range 0 h 10)]
      (line 0 y w y))))

(defn draw-board [cells]
  (smooth)
  (background 255)
  (draw-grid)
  ;;(stroke 10 200 10)
  ;;(stroke-weight 1)
  (fill 10 200 10 200)
  (doseq [[x y] cells]
    (ellipse (+ (* x 10) 5)
             (+ (* y 10) 5)
             8 8)))

(defn- cell-coordinate [n]
  (/ (- n (mod n 10)) 10))

(defn mouse-clicked []
  (let [x (mouse-x)
        cx (cell-coordinate x)
        y (mouse-y)
        cy (cell-coordinate y)]
    (if (contains? @board [cx cy])
      (swap! board disj [cx cy])
      (swap! board conj [cx cy]))
    (draw-board @board) ))

(defn draw []
  (when @running
    (swap! board step)
    (draw-board @board)))

(ns triangles
  (:import [java.awt Color Dimension Point])
  (:import [java.awt.image BufferedImage])
  (:import [java.awt.geom Point2D])
  (:import [javax.swing JLabel JFrame])
  (:import [java.util.concurrent Executors TimeUnit]))

(defn draw-point [graphics [x y]] (.drawLine graphics x y x y))

(defn middle-of [[x1 y1] [x2 y2]] [(/ (+ x1 x2) 2.0) (/ (+ y1 y2) 2.0)])

(defn draw-triangles [width height points graphics]
  (let [vertices [[(/ width 2) 0] [0 height] [width height]]
        random (java.util.Random.)]
    (loop [i points p (middle-of [0 0] [width height])]
      (if (> i 0)
        (let [p2 (middle-of p (vertices (.nextInt random 3)))]
          (draw-point graphics p2)
          (recur (dec i) p2))))))

(defn draw [width height points]
  (let [image (BufferedImage. width height BufferedImage/TYPE_INT_RGB)
        canvas (proxy [JLabel] [] (paint [g] (.drawImage g image 0 0 this)))
        graphics (.createGraphics image)]
    (doto (JFrame.) (.add canvas) (.setSize (Dimension. (+ 50 width) (+ 50 height))) (.show))
    (.scheduleAtFixedRate (Executors/newSingleThreadScheduledExecutor) #(.repaint canvas) 50 50 TimeUnit/MILLISECONDS)
    (draw-triangles (- width 1) (- height 1) points graphics)))


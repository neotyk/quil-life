(ns quil-life.core
  (:gen-class
   :extends java.applet.Applet)
  (:use [quil core applet])
  (:require [quil-life.dynamic :as dynamic]))

(defapplet example
  :title "life"
  :setup dynamic/setup
  :draw dynamic/draw
  :key-typed dynamic/key-typed
  :mouse-clicked dynamic/mouse-clicked
  :size [200 200])

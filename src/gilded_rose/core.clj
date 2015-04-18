(ns gilded-rose.core
  (:require [gilded-rose.item-quality :refer [update]]))

(defn- degradable-item? [{name :name}]
  (not (.contains name "Sulfuras, Hand of Ragnaros")))

(defn- age-one-day [{sell-in :sell-in :as item}]
  (assoc item :sell-in (dec sell-in)))

(def ^:private all-age-one-day
  (partial map #(if (degradable-item? %) (age-one-day %) %)))

(defn update-quality [items]
  (map update (all-age-one-day items)))

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn update-current-inventory[]
  (let [inventory 
        [(item "+5 Dexterity Vest" 10 20)
         (item "Aged Brie" 2 0)
         (item "Elixir of the Mongoose" 5 7)
         (item "Sulfuras, Hand of Ragnaros" 0 80)
         (item "Backstage passes to a TAFKAL80ETC concert" 15 20)]]
    (update-quality inventory)))
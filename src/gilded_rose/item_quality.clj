(ns gilded-rose.item-quality)

(defn- update-quality [item value]
  (merge item {:quality value}))

(defn- increase-quality [{:keys [quality] :as item} times]
  (update-quality item (min 50 (reduce + quality (repeat times 1)))))

(defn- decrease-quality [{:keys [quality] :as item} times]
  (update-quality item (max 0 (reduce - quality (repeat times 1)))))

(defn- set-quality-to-zero [{:keys [quality] :as item}]
  (update-quality item 0))

(defn- after-selling-date? [{sell-in :sell-in}]
  (< sell-in 0))

(defn- ten-or-more-days-to-selling-date? [{sell-in :sell-in}]
  (>= sell-in 10))

(defn- between-days-to-selling-date? [lower higher {sell-in :sell-in}]
  (and (>= sell-in lower) (< sell-in higher)))

(defn- update-regular-item-quality [item]
  (if (after-selling-date? item)  
    (decrease-quality item 2)
    (decrease-quality item 1)))

(defmulti update 
  (fn [{name :name}]
    (if (.contains name "Conjured")
      "Conjured"
      name)))

(defmethod update "Conjured" [{name :name :as item}]
  (let 
    [not-conjured-item-name (clojure.string/replace name #"Conjured " "")
     not-conjured-item (merge item {:name not-conjured-item-name})]
    (merge (update (update not-conjured-item))
           {:name name})))

(defmethod update :default [item]
  item)

(defmethod update "Aged Brie" [item]
  (increase-quality item 1))

(defmethod update "Backstage passes to a TAFKAL80ETC concert" [item]
  (cond 
    (ten-or-more-days-to-selling-date? item) (increase-quality item 1)
    
    (between-days-to-selling-date? 5 10 item) (increase-quality item 2)
    
    (between-days-to-selling-date? 0 5 item) (increase-quality item 3)
    
    (after-selling-date? item) (set-quality-to-zero item)
    
    :else item))

(defmethod update "+5 Dexterity Vest" [item]
  (update-regular-item-quality item))

(defmethod update "Elixir of the Mongoose" [item]
  (update-regular-item-quality item))
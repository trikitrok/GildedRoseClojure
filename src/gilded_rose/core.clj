(ns gilded-rose.core)

(defn- regular? [{:keys [name]}]
  (or (= "+5 Dexterity Vest" name) 
      (= "Elixir of the Mongoose" name)))

(defn- aged-brie? [{:keys [name]}]
  (= name "Aged Brie"))

(defn- backstage-passes? [{:keys [name]}]
  (= name "Backstage passes to a TAFKAL80ETC concert"))

(defn- increase-quality [{:keys [quality] :as item} times]
  (merge item
         {:quality (min 50 (reduce + quality (repeat times 1)))}))

(defn- decrease-quality [{:keys [quality] :as item} times]
  (merge item 
         {:quality (max 0 (reduce - quality (repeat times 1)))}))

(defn- set-quality-to-zero [{:keys [quality] :as item}]
  (merge item {:quality 0}))

(defn- after-selling-date? [{sell-in :sell-in}]
  (< sell-in 0))

(defn- ten-or-more-days-to-selling-date? [{sell-in :sell-in}]
  (>= sell-in 10))

(defn- between-days-to-selling-date? [lower higher {sell-in :sell-in}]
  (and (>= sell-in lower) (< sell-in higher)))

(defn- update-item-quality [item]
  (cond
    (aged-brie? item) (increase-quality item 1)    
    
    (backstage-passes? item)
    (cond 
      (ten-or-more-days-to-selling-date? item) (increase-quality item 1)
      
      (between-days-to-selling-date? 5 10 item) (increase-quality item 2)
      
      (between-days-to-selling-date? 0 5 item) (increase-quality item 3)
      
      (after-selling-date? item) (set-quality-to-zero item)
      
      :else item)
    
    (regular? item)
    (if (after-selling-date? item)  
      (decrease-quality item 2)
      (decrease-quality item 1))
    
    :else item))

(defn update-quality [items]
  (map
    update-item-quality
    (map (fn [item]
           (if (not= "Sulfuras, Hand of Ragnaros" (:name item))
             (merge item {:sell-in (dec (:sell-in item))})
             item))
         items)))

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn update-current-inventory[]
  (let [inventory 
        [
         (item "+5 Dexterity Vest" 10 20)
         (item "Aged Brie" 2 0)
         (item "Elixir of the Mongoose" 5 7)
         (item "Sulfuras, Hand of Ragnaros" 0 80)
         (item "Backstage passes to a TAFKAL80ETC concert" 15 20)
         ]]
    (update-quality inventory)))
(ns gilded-rose.core)

(defn- regular? [{:keys [name]}]
  (or (= "+5 Dexterity Vest" name) 
      (= "Elixir of the Mongoose" name)))

(defn- aged-brie? [{:keys [name]}]
  (= name "Aged Brie"))

(defn update-quality [items]
  (map
    (fn [{:keys [sell-in name quality] :as item}] 
      (cond
        (and (< sell-in 0) 
             (= "Backstage passes to a TAFKAL80ETC concert" name))
        (merge item {:quality 0})
        
        (aged-brie? item)
        (if (< quality 50)
          (merge item {:quality (inc quality)})
          item)
        
        (= name "Backstage passes to a TAFKAL80ETC concert")
        (cond 
          (and (>= sell-in 5) (< sell-in 10))
              (merge item {:quality (min 50 (inc (inc quality)))})
          
          (and (>= sell-in 0) (< sell-in 5))
            (merge item {:quality (min 50 (inc (inc (inc quality))))})
            
          (< quality 50)
              (merge item {:quality (inc quality)})
          
          :else item)
                
        (regular? item)
        (if (< sell-in 0)  
          (merge item {:quality (max 0 (- quality 2))})
          (merge item {:quality (max 0 (dec quality))}))
        
        :else item))
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
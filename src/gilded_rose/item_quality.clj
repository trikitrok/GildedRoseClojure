(ns gilded-rose.item-quality)

(defn- update-quality [item value]
  (assoc item :quality value))

(defn- increase-quality [{:keys [quality] :as item} times]
  (update-quality item (min 50 (reduce + quality (repeat times 1)))))

(defn- decrease-quality [{:keys [quality] :as item} times]
  (update-quality item (max 0 (reduce - quality (repeat times 1)))))

(defn- set-quality-to-zero [item]
  (update-quality item 0))

(defn- after-selling-date? [{sell-in :sell-in}]
  (< sell-in 0))

(defn- ten-or-more-days-to-selling-date? [{sell-in :sell-in}]
  (>= sell-in 10))

(defn- between-days-to-selling-date? [lower higher {sell-in :sell-in}]
  (and (>= sell-in lower) (< sell-in higher)))

(defn- type-of-item [{name :name}]
  (let [item-types-by-name
        {"Aged Brie" :aged-brie
         "Backstage passes to a TAFKAL80ETC concert" :backstage-pass
         "+5 Dexterity Vest" :regular-item
         "Elixir of the Mongoose" :regular-item}]
    (if (.contains name "Conjured")
      :conjured
      (item-types-by-name name))))

(defmulti update type-of-item)

(defmethod update :conjured [{name :name :as item}]
  (let
    [not-conjured-item-name (clojure.string/replace name #"Conjured " "")
     not-conjured-item      (assoc item :name not-conjured-item-name)]
    (assoc (update (update not-conjured-item))
      :name name)))

(defmethod update :default [item]
  item)

(defmethod update :aged-brie [item]
  (increase-quality item 1))

(defmethod update :backstage-pass [item]
  (cond
    (ten-or-more-days-to-selling-date? item) (increase-quality item 1)

    (between-days-to-selling-date? 5 10 item) (increase-quality item 2)

    (between-days-to-selling-date? 0 5 item) (increase-quality item 3)

    (after-selling-date? item) (set-quality-to-zero item)

    :else item))

(defmethod update :regular-item [item]
  (if (after-selling-date? item)
    (decrease-quality item 2)
    (decrease-quality item 1)))
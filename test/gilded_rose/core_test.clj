(ns gilded-rose.core-test
  (:use midje.sweet)
  (:use [gilded-rose.core]))

(defn pass-days [n inventory]
  (nth (iterate update-quality inventory) n))

(facts 
  "about gilded rose"
  
  (facts 
    "Sulfuras"
    (pass-days 
      5 
      [(item "Sulfuras, Hand of Ragnaros" 0 80)]) => [(item "Sulfuras, Hand of Ragnaros" 0 80)])
  
  (facts
    "Regular items"
    
    (facts
      "Quality decreases one by one before selling date"
      (pass-days 
        5
        [(item "+5 Dexterity Vest" 10 20)]) => [(item "+5 Dexterity Vest" 5 15)]
      
      (pass-days 
        5
        [(item "Elixir of the Mongoose" 5 7)]) => [(item "Elixir of the Mongoose" 0 2)])
    
    (fact
      "Once the sell by date has passed, quality degrades twice as fast"
      
      (pass-days 
        10
        [(item "+5 Dexterity Vest" 0 20)]) => [(item "+5 Dexterity Vest" -10 0)])
    
    (fact
      "The quality of an item is never negative"
      
      (pass-days 
        20
        [(item "+5 Dexterity Vest" 10 20)]) => [(item "+5 Dexterity Vest" -10 0)]))
  
  (facts 
    "Aged Brie"
    
    (fact
      "Quality increases by one before sell date"  
      (pass-days 
        2
        [(item "Aged Brie" 2 0)]) => [(item "Aged Brie" 0 2)])
    
    (fact
      "Quality also increases by one after sell date"  
      (pass-days 
        4
        [(item "Aged Brie" 2 0)]) => [(item "Aged Brie" -2 4)])
    
    (fact
      "Quality can't be greater than 50"  
      (pass-days 
        51
        [(item "Aged Brie" 2 0)]) => [(item "Aged Brie" -49 50)]))
  
  (facts 
    "Backstage passages"
    
    (fact 
      "Before 10 days of the concert quality increases one by one"
      (pass-days 
        5
        [(item "Backstage passes to a TAFKAL80ETC concert" 15 20)]) 
      => [(item "Backstage passes to a TAFKAL80ETC concert" 10 25)])
    
    (fact 
      "Between 10 and 5 days of the concert quality increases two by two"
      (pass-days 
        5
        [(item "Backstage passes to a TAFKAL80ETC concert" 10 20)]) 
      => [(item "Backstage passes to a TAFKAL80ETC concert" 5 30)])
    
    (fact 
      "The last 5 days before the concert quality increases three by three"
      (pass-days 
        5
        [(item "Backstage passes to a TAFKAL80ETC concert" 5 20)]) 
      => [(item "Backstage passes to a TAFKAL80ETC concert" 0 35)])
    
    (fact 
      "After the concert quality is 0"
      (pass-days 
        6
        [(item "Backstage passes to a TAFKAL80ETC concert" 5 20)]) 
      => [(item "Backstage passes to a TAFKAL80ETC concert" -1 0)])
    
    (fact 
      "Quality can't be grater than 50"
      (pass-days 
        2
        [(item "Backstage passes to a TAFKAL80ETC concert" 10 48)]) 
      => [(item "Backstage passes to a TAFKAL80ETC concert" 8 50)]
      
      (pass-days 
        17
        [(item "Backstage passes to a TAFKAL80ETC concert" 17 20)]) 
      => [(item "Backstage passes to a TAFKAL80ETC concert" 0 50)]))
  
  (facts
    "Conjured items"
    (fact 
      "Quality decreases by two each day before sell date"
      (pass-days 
        2
        [(item "Conjured Elixir of the Mongoose" 17 20)]) 
      => [(item "Conjured Elixir of the Mongoose" 15 16)])
    
    (fact 
      "Quality decreases by four each day after sell date"
      (pass-days 
        2
        [(item "Conjured Elixir of the Mongoose" 0 20)]) 
      => [(item "Conjured Elixir of the Mongoose" -2 12)])
    
    (fact 
      "Quality can't be less than zero"
      (pass-days 
        1
        [(item "Conjured Elixir of the Mongoose" 2 1)]) 
      => [(item "Conjured Elixir of the Mongoose" 1 0)])))
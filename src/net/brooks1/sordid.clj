(ns net.brooks1.sordid
 (:import (clojure.lang Seqable Sequential ISeq IPersistentSet ILookup
                        IPersistentStack IPersistentCollection Associative
                        Sorted Reversible Indexed Counted IHashEq)))

(declare sordid-map)

(deftype SordidMap [m mdata params]
  java.lang.Object
    (equals [_ x] (= m x))
    (hashCode [this] (clojure.lang.Util/hash this))

  java.util.Map
    (size [_] (count m))
    (containsKey [_ k] (boolean (m k)))
    (get [_ k] (get m k))

  java.lang.Iterable
    (iterator [this]
      (clojure.lang.SeqIterator. (seq this)))

  clojure.lang.IObj
    (meta [_] mdata)
    (withMeta [_ mdata] (sordid-map (with-meta m mdata)))

  #_clojure.lang.IHashEq
  #_(hasheq [this] (hash-ordered this))

  clojure.lang.Sequential
  clojure.lang.Seqable
    (seq [this] (when (seq m) this)) ;; ?

  clojure.lang.ISeq
    (first [_] (first m))
    (next [_] (next m))

  clojure.lang.IPersistentCollection
    (equiv [_ x] (= m x))
    (empty [_] (sordid-map (with-meta (empty m) mdata)))
    (cons  [_ b] (sordid-map (conj m b)))

  clojure.lang.IPersistentMap
  (assoc [_ k v] (let [limit (dec (:size params))]
                   (->
                    (if (<= (count m) limit)
                      (assoc m k v)
                      (into (empty m) (conj (take limit m) [k v])))
                    (with-meta mdata)
                    (sordid-map params))))
    (assocEx [_ k v] )
    (without [_ k] )

  clojure.lang.MapEquivalence
  ;; No methods

  clojure.lang.Reversible
    (rseq [_] (rseq m))

  clojure.lang.IMapIterable
    (keyIterator [_] )
    (valIterator [_] )

    #_(comment

        clojure.lang.AFn
        java.util.Map
        java.io.Serializable
        clojure.lang.Associative
        clojure.lang.IHashEq
        java.util.concurrent.Callable
        clojure.lang.IFn
        java.lang.Iterable
        clojure.lang.IPersistentMap
        clojure.lang.ILookup
        clojure.lang.APersistentMap
        clojure.lang.IKVReduce
        clojure.lang.IPersistentCollection
        java.lang.Object
        clojure.lang.IMeta
        clojure.lang.IObj
        clojure.lang.IEditableCollection
        clojure.lang.MapEquivalence
        clojure.lang.Counted
        clojure.lang.IMapIterable
        clojure.lang.Seqable
        java.lang.Runnable

))

;; map, meta, state, spec
(defn sordid-map [m & [[params] state]]
  (SordidMap. m (meta m) {:size 10}))

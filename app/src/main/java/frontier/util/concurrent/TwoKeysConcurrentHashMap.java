/*
 * Copyright (C) 2017 kkoudev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package frontier.util.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * キー値を 2 つもつ同期型ハッシュマップ。<br>
 * <br>
 * キー1に重複しやすい値を設定し、キー2にそれよりは重複しにくい値を設定すると<br>
 * 使用メモリを削減することが可能である。<br>
 * <br>
 *
 * @param <K1>  1つ目のキーのクラス
 * @param <K2>  2つ目のキーのクラス
 * @param <V>   値のクラス
 *
 * @author Kou
 *
 */
public class TwoKeysConcurrentHashMap<K1, K2, V> {


    /**
     * ベースとなるハッシュマップ
     */
    private final ConcurrentMap<K1, ConcurrentMap<K2, V>>               baseTable;

    /**
     * 利用するロックオブジェクトテーブル
     */
    private final ConcurrentMap<ConcurrentMap<K2, V>, ReentrantLock>    mapLocks =
        new ConcurrentHashMap<ConcurrentMap<K2, V>, ReentrantLock>();



    /**
     * デフォルト初期容量でハッシュマップを作成する。
     *
     */
    public TwoKeysConcurrentHashMap() {

        // ハッシュマップを作成
        baseTable = new ConcurrentHashMap<K1, ConcurrentMap<K2, V>>();

    }


    /**
     * 初期容量を指定してハッシュマップを作成する。
     *
     * @param initialCapacity 初期容量
     */
    public TwoKeysConcurrentHashMap(
            final int initialCapacity
            ) {

        // ハッシュマップを作成
        baseTable = new ConcurrentHashMap<K1, ConcurrentMap<K2, V>>(initialCapacity);

    }


    /**
     * 指定された 2 つのキーで、値をマッピングする。
     *
     * @param key1  ハッシュマップのキー1
     * @param key2  ハッシュマップのキー2
     * @param value マッピングする値
     * @return 前回マッピングされていた値
     */
    public V put(
            final K1        key1,
            final K2        key2,
            final V         value
            ) {

        return put(key1, key2, value, false);

    }


    /**
     * 指定されたキーが値と関連付けられていない場合は、指定された値に関連付ける。<br>
     * <br>
     * これは以下の式と等価となる。<br>
     * <pre>
     * if (!map.containsKey(key1, key2)) {
     *     return map.put(key1, key2, value);
     * } else {
     *     return map.get(key1, key2);
     * }
     * </pre>
     * 但し、アクションが原子的に実行される点が異なる。<br>
     * <br>
     * @param key1  ハッシュマップのキー1
     * @param key2  ハッシュマップのキー2
     * @param value マッピングする値
     * @return 前回マッピングされていた値
     */
    public V putIfAbsent(
            final K1        key1,
            final K2        key2,
            final V         value
            ) {

        return put(key1, key2, value, true);

    }


    /**
     * 指定された 2 つのキーで、値をマッピングする。
     *
     * @param key1          ハッシュマップのキー1
     * @param key2          ハッシュマップのキー2
     * @param value         マッピングする値
     * @param onlyIfAbsent  対応するキーがないときのみ値を追加するかどうか
     * @return 前回マッピングされていた値
     */
    private V put(
            final K1        key1,
            final K2        key2,
            final V         value,
            final boolean   onlyIfAbsent
            ) {

        ConcurrentMap<K2, V>    entryKey1;      // キー1のエントリ
        ReentrantLock           entryLock1;     // キー1のエントリロックオブジェクト


        // キー1のエントリを取得する
        entryKey1 = baseTable.get(key1);

        // 取得できなかった場合
        if (entryKey1 == null) {

            // キー1のエントリを作成する
            entryKey1 = new ConcurrentHashMap<K2, V>();

        }

        // キー1のエントリロックオブジェクトを取得する
        entryLock1 = mapLocks.get(entryKey1);

        // 取得できなかった場合
        if (entryLock1 == null) {

            // ロックオブジェクトを作成する
            entryLock1 = new ReentrantLock();

            // ロックオブジェクトテーブルへ追加する
            mapLocks.put(entryKey1, entryLock1);

        }


        // キー1のエントリをロックする
        entryLock1.lock();

        try {

            final V    oldMappingValue;   // 前回マッピング値


            // 対応するキーがないときのみ値を追加する場合
            if (onlyIfAbsent) {

                // キー2がない場合
                if (!entryKey1.containsKey(key2)) {

                    // キー1のエントリへ、キー2を使用して値を挿入する
                    oldMappingValue = entryKey1.put(key2, value);

                    // キー1のエントリをベーステーブルに挿入
                    baseTable.put(key1, entryKey1);

                } else {

                    // キー1のエントリからキー2を使用して値を取得する
                    oldMappingValue = entryKey1.get(key2);

                }

            } else {

                // キー1のエントリへ、キー2を使用して値を挿入する
                oldMappingValue = entryKey1.put(key2, value);

                // キー1のエントリをベーステーブルに挿入
                baseTable.put(key1, entryKey1);

            }


            // 前回マッピング値を返却する
            return oldMappingValue;

        } finally {

            // ロックを解除する
            entryLock1.unlock();

        }

    }


    /**
     * 指定された 2 つのキーにマッピングされた値を取得する。
     *
     * @param key1  ハッシュマップのキー1
     * @param key2  ハッシュマップのキー2
     * @return マッピングされた値
     */
    public V get(
            final K1    key1,
            final K2    key2
            ) {

        ConcurrentMap<K2, V>    entryKey1;      // キー1のエントリ
        ReentrantLock           entryLock1;     // キー1のエントリロックオブジェクト


        // キー1のエントリを取得する
        entryKey1 = baseTable.get(key1);

        // 取得できなかった場合
        if (entryKey1 == null) {

            // キー1のエントリを作成する
            entryKey1 = new ConcurrentHashMap<K2, V>();

        }

        // キー1のエントリロックオブジェクトを取得する
        entryLock1 = mapLocks.get(entryKey1);

        // 取得できなかった場合
        if (entryLock1 == null) {

            // ロックオブジェクトを作成する
            entryLock1 = new ReentrantLock();

            // ロックオブジェクトテーブルへ追加する
            mapLocks.put(entryKey1, entryLock1);

        }


        // キー1のエントリをロックする
        entryLock1.lock();

        try {

            // ベースエントリテーブルにキー1が含まれている場合
            if (baseTable.containsKey(key1)) {

                // キー2を利用して値を取得する
                return entryKey1.get(key2);

            } else {

                // nullを返す
                return null;

            }

        } finally {

            // ロックを解除する
            entryLock1.unlock();

        }

    }


    /**
     * 指定された 2 つのキーにマッピングされた値が存在するかどうかを取得する。
     *
     * @param key1  ハッシュマップのキー1
     * @param key2  ハッシュマップのキー2
     * @return マッピングされた値が存在する場合は true
     */
    public boolean containsKey(
            final K1    key1,
            final K2    key2
            ) {

        ConcurrentMap<K2, V>    entryKey1;      // キー1のエントリ
        ReentrantLock           entryLock1;     // キー1のエントリロックオブジェクト


        // キー1のエントリを取得する
        entryKey1 = baseTable.get(key1);

        // 取得できなかった場合
        if (entryKey1 == null) {

            // キー1のエントリを作成する
            entryKey1 = new ConcurrentHashMap<K2, V>();

        }

        // キー1のエントリロックオブジェクトを取得する
        entryLock1 = mapLocks.get(entryKey1);

        // 取得できなかった場合
        if (entryLock1 == null) {

            // ロックオブジェクトを作成する
            entryLock1 = new ReentrantLock();

            // ロックオブジェクトテーブルへ追加する
            mapLocks.put(entryKey1, entryLock1);

        }


        // キー1のエントリをロックする
        entryLock1.lock();

        try {

            // 基本テーブルにエントリがまだ存在している場合
            // かつキー1のエントリから、キー2を使用して値が存在するかどうかを取得する
            return baseTable.containsKey(key1) && entryKey1.containsKey(key2);

        } finally {

            // ロックを解除する
            entryLock1.unlock();

        }

    }


    /**
     * 指定された 2 つのキーにマッピングされた値を削除する。
     *
     * @param key1  ハッシュマップのキー1
     * @param key2  ハッシュマップのキー2
     * @return 削除された値
     */
    public V remove(
            final K1    key1,
            final K2    key2
            ) {

        ConcurrentMap<K2, V>    entryKey1;      // キー1のエントリ
        ReentrantLock           entryLock1;     // キー1のエントリロックオブジェクト


        // キー1のエントリを取得する
        entryKey1 = baseTable.get(key1);

        // 取得できなかった場合
        if (entryKey1 == null) {

            // キー1のエントリを作成する
            entryKey1 = new ConcurrentHashMap<K2, V>();

        }

        // キー1のエントリロックオブジェクトを取得する
        entryLock1 = mapLocks.get(entryKey1);

        // 取得できなかった場合
        if (entryLock1 == null) {

            // ロックオブジェクトを作成する
            entryLock1 = new ReentrantLock();

            // ロックオブジェクトテーブルへ追加する
            mapLocks.put(entryKey1, entryLock1);

        }


        // キー1のエントリをロックする
        entryLock1.lock();


        final V  removeValue;      // 削除した値

        try {

            // キー1のエントリから、キー2を使用して値を削除する
            removeValue = entryKey1.remove(key2);

            // エントリが空になった場合
            if (entryKey1.isEmpty()) {

                // エントリを削除する
                baseTable.remove(key1);

                // ロックオブジェクトを削除する
                mapLocks.remove(entryKey1);

            }

        } finally {

            // ロックを解除する
            entryLock1.unlock();

        }

        // 削除した値を返す
        return removeValue;

    }


    /**
     * ハッシュマップの内容を全消去する。
     *
     */
    public void clear() {

        // ベーステーブルをクリアする
        baseTable.clear();

    }


    /**
     * ハッシュマップ内の値の総数を取得する。
     *
     * @return ハッシュマップ内の値の総数
     */
    public int size() {

        int retSize = 0;    // 返却するハッシュマップ内の値の総数


        // キー1のエントリを全検索
        for (final ConcurrentMap<K2, V> entry : baseTable.values()) {

            // エントリのサイズを加算する
            retSize += entry.size();

        }

        // 算出したサイズを返す
        return retSize;

    }


}

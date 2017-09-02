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
package frontier.util;


/**
 * 任意の型定義が可能な名称付値データクラス。
 *
 * @param <T> 値の型
 * @author Kou
 *
 */
public class TypeNameValuePair<T> {


    /**
     * 名前
     */
    private final String        name;

    /**
     * 値
     */
    private final T             value;



    /**
     * 名前と値のペアを作成する。
     *
     * @param argName   名前
     * @param argValue  値
     */
    public TypeNameValuePair(
            final String    argName,
            final T         argValue
            ) {

        name  = argName;
        value = argValue;

    }


    /**
     * 名前を取得する。
     *
     * @return 名前
     */
    public String getName() {

        return name;

    }


    /**
     * 値を取得する。
     *
     * @return 値
     */
    public T getValue() {

        return value;

    }


}

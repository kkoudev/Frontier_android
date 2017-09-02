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
 * リフレクション例外クラス。
 *
 * @author Kou
 *
 */
public class ReflectException extends Exception {


    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 1L;




    /**
     * パラメータを指定しないで例外クラスを作成する。
     *
     */
    public ReflectException() {

        super();

    }


    /**
     * 詳細メッセージを指定して例外クラスを作成する。
     *
     * @param detailMessage 詳細メッセージ
     */
    public ReflectException(
            final String    detailMessage
            ) {

        super(detailMessage);

    }


    /**
     * 例外クラスを指定して例外クラスを作成する。
     *
     * @param exception     例外クラス
     */
    public ReflectException(
            final Throwable exception
            ) {

        super(exception);

    }


    /**
     * 詳細メッセージと例外クラスを指定して例外クラスを作成する。
     *
     * @param detailMessage 詳細メッセージ
     * @param exception     例外クラス
     */
    public ReflectException(
            final String    detailMessage,
            final Throwable exception
            ) {

        super(detailMessage, exception);

    }

}

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
package frontier.app;

import frontier.util.TypeNameValuePair;





/**
 * フレームワークで利用される汎用型パラメータの名称付き値データクラス。
 *
 * @author Kou
 *
 */
public class FRNameValuePair extends TypeNameValuePair<Object> {


    /**
     * 名前と値のペアを作成する。
     *
     * @param argName   名前
     * @param argValue  値
     */
    public FRNameValuePair(
            final String    argName,
            final Object    argValue
            ) {

        super(argName, argValue);

    }


}

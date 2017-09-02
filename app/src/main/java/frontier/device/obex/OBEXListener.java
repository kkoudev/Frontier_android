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
package frontier.device.obex;



/**
 * OBEXレスポンス処理リスナー。
 *
 * @author Kou
 *
 */
public interface OBEXListener {


    /**
     * リクエストOBEXオペレーションを取得する。
     *
     * @return リクエストするOBEXオペレーション
     */
    OBEXOperation[] getRequestObexOperation();


    /**
     * OBEXオペレーションレスポンス処理を実行する。<br>
     * <br>
     * 処理したオペレーションごとにレスポンスを本メソッドで返却する。<br>
     * 対応オペレーションコードがない場合、<br>
     * オペレーション実行前にエラーが発生したこととなる。<br>
     *
     * @param session   セッションのインスタンス
     * @param opecode   対応オペレーションコード
     * @param response  レスポンスデータ
     */
    void responseObexOperation(
            final OBEXSession       session,
            final OBEXOperationCode opecode,
            final OBEXResponse      response
            );


}

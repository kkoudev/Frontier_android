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
package frontier.net;



/**
 * HTTP通信完了通知先リスナー。
 *
 * @author Kou
 *
 */
public interface HttpListener {


    /**
     * 通信完了処理を実行する。
     *
     * @param result    通信結果データ
     */
    void connectFinished(
            final HttpResult    result
            );


}

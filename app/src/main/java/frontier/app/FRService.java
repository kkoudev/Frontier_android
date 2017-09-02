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

import android.app.Service;
import android.os.Binder;


/**
 * 機能拡張サービスのスーパークラス。
 *
 * @author Kou
 *
 */
public abstract class FRService extends Service {


    /**
     * 各種クラスをロードする
     */
    static {

        // システムで利用するクラスをロードする
        FRClassInitializer.loadClasses();

    }



    /**
     * サービスバインダークラス。
     *
     * @author Kou
     *
     */
    public class FRBinder extends Binder {


        /**
         * サービスのインスタンスを取得する。
         *
         * @return サービスのインスタンス
         */
        public Service getService() {

            return FRService.this;

        }


    }


}

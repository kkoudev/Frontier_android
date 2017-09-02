# Frontier andoridライブラリについて

本ライブラリは、Android 2.3以降を対象としたライブラリです。

Androidにおけるアクティビティの起動を始めとした処理は<br>
1stepで記述できるものが殆どなく、<br>
簡単な処理であっても、2~3stepで処理を記述しなければならないことがあります。<br>
これは特にAndroidを始めたばかりの方には面倒に感じてしまうことも多いかと思われます。<br>
<br>
本ライブラリではそのような悩みを極力解決しようとしたものです。<br>
代表的な機能としては、以下のものがございます。<br>

01. [アクティビティのメンバ変数の自動保存＆復帰]<br>
FRActivity, FRListActivity, FRPreferenceActivityの各アクティビティクラスを継承して利用するだけで、メンバ変数を自動的にonSaveInstanceStateで保存し、onRestoreInstanceStateで復帰する仕組みもございます。<br>
そのため、今までわざわざ手書きで記述しなければならなかった画面切り替え時のメンバ変数の保存処理や、メモリ不足時のメンバ変数保存処理を自前で記述する必要がなくなります。<br>

02. [O/Rマッピングによるデータベースアクセス]<br>
iBATIS(MyBatis)の設計を継承したO/Rマッピング機能を利用することで<br>
SQLiteデータベースへアクセスする機能も存在します。<br>
frontier.db パッケージ以下のクラスを主に利用します。<br>
(こちらの処理は [FrontierDao](https://github.com/kkoudev/FrontierDao)ライブラリとして独立させております)<br>

03. [豊富なユーティリティクラス]<br>
frontier.util以下にあるクラスは、文字列操作や日付操作を始めとした<br>
手間のかかる処理を簡略化することができるユーティリティクラスがございます。<br>
Base64クラスについては、Android 2.2以降で用意されている同名クラスの何倍も高速に処理できるようになっています。


【詳しい使い方や問い合わせなど】<br>
こちらのブログで使い方を随時記載しております。<br>
http://mobileapplication.blog.fc2.com/
<br>
<br>
※2017年現時点ではブログはこちらで執筆しております。<br>
https://medium.com/@kkoudev/

## [Changelog](CHANGELOG.md)

## [License](LICENSE)

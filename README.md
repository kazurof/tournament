# tournament

このリポジトリは、第７回日曜数学会 https://www.facebook.com/events/858311494303516/ の発表で使ったコードです。

# 実行のさせ方

## 全組み合わせの出力

`>gradlew generate`

トーナメントの組み合わせを全てファイルに出力します。パラメータで、優勝するための試合数を指定できます。

`>gradlew generate -DnumOfGame=2`

ならば、試合数 2 のトーナメントです。（参加人数4人) 試合数 4 の場合は、16人、試合数8の場合は32人トーナメントです。
（デフォルト3）

出力結果は、`data.<numOfGame>.tsv` というファイルに出力されます。

## 組み合わせの解析

`>gradlew analyse` 

出力したファイルを解析し、結果をファイルに出力します。
パラメータで、解析対象の試合数のファイルと、出力の形式を指定できます。

- `-Dtype=raw`　組み合わせの値をそのまま出力します。
- `-Dtype=frac`　組み合わせの値と全体数の比を出力します。
- `-Dtype=factor`　組み合わせの値と全体数の比を出力します。数字はそれぞれ因数分解されます。

実行例は以下のとおりです。

`>gradlew generate -DnumOfGame=2　-Dtype=raw` 


出力結果は、`result.<numOfGame>.<type>.tsv` というファイルに出力されます。(typeの名前は少し異なりますが、気にせずに。)




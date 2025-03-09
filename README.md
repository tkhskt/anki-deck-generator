# anki-deck-generator

英辞郎の辞書データを元にankiのデッキを作成する。

## バイナリの作成

- mac
  - `packageDmg`

## デッキのフォーマット

生成されるデッキのフォーマットは下記の通り。

### 表面

```
assurance

э∫u'(э)rэns

 German officials have offered assurances that the incident will be pursued vigorously.
```

### 裏面

```
【名】

1. 断言すること、請け合うこと
2. 言質、請け合う約束
3. 〔経験から分かる〕確実さ、確かさ
4. 自信、確信
5. 〈英〉生命保険

ドイツ当局者は、全力を傾けてこの事件を追求すると保証しました。
```

## 使い方

### 辞書データの準備

辞書データは購入する必要がある。

https://booth.pm/ja/items/777563

取得した辞書データのテキストファイルをUTF-8に変換した後、`resource`ディレクトリ下に`dictionary`ディレクトリを作成し、辞書データのテキストファイルを配置する。

### ankiデッキ化対象の単語リストの準備

ankiデッキとして作成したい単熟語をまとめたCSVファイルを作成し、プロジェクトのルートに配置する。

CSVのフォーマットは`単熟語, 品詞`のフォーマットにする必要がある。

```csv
respect,名
respectful,形
respectable,形
respective,形
willpower,名
individual,形
individuality,名
assure,動
```

対応する品詞は下記の通り。

- 名
  - 名詞
- 動
  - 動詞
- 形
  - 形容詞
- 副
  - 副詞
- 助
  - 助動詞

### 入力/出力ファイル名の指定

`Main.kt`で辞書データのファイルパスと出力するデッキ名、デッキ化対象の単語リストのファイル名を指定する。

デフォルトは下記の通り

- 辞書データ
  - `dictionary/eijiro.txt`
- デッキ名
  - `deck`
- デッキ化対象の単語リストのファイル名
  - `input.csv`

カードのセパレータと表/裏のセパレータも`Main.kt`で指定可能。

# 小人さんスクリプト集

ちょっとしたことを便利にするスクリプトを集めているプロジェクトです。

現在、以下のようなことができます。

* Backlog の課題から、直近の #IWStudy の開催日程を取得して、Typetalk のトピックにリマインドしてくれる

## Project setup

_このプロジェクトを始める開発者の方へ_

1. このプロジェクトは [Groovy](http://groovy.codehaus.org/Japanese+Home) と [Gradle](http://www.gradle.org/) を利用しています。どちらも [GVM](http://gvmtool.net/) でインストールできるので、利用すると良いと思います。
	* Gradle は、Gradle Wrapper を利用しているのでご自身の端末にインストールしなくても、/gradlew を叩けば勝手にダウンロードされます。 
2. エディタは [Intellij IDEA](http://www.jetbrains.com/idea/) をオススメしますが、他の IDE などでも構いません。お使いのエディタや IDE に合わせて、[http://www.gitignore.io/](http://www.gitignore.io/) にて .gitignore ファイルを生成して適宜追加してください。
3. ビルド時やテスト時は、[CloudBees](http://www.cloudbees.com/) の Maven リポジトリの認証情報及び、Backlog/Typetalk の認証情報が必要となります。ローカル端末のホームディレクトリ配下に `.gradle/gradle.properties` というファイルを作成し、以下の内容で個人情報を設定することで、ビルドが通るようになります。

```
password=`CloudBeesのパスワード`
backlogPassword=`Backlogのパスワード`
clientId=`Typetalkで発行したアプリケーションのクライアントID`
clientSecret=`Typetalkで発行したアプリケーションのクライアントシークレット`
```

## Testing

テストフレームワークは [Spock](http://docs.spockframework.org/en/latest/) を利用しています。Gradle で test タスクを実行することで、自動テストが実行されます。

### Unit Tests

    $ ./gradlew test

## Build, Deploy, and Release

### Development environment

* さくらVPSのサーバを利用しています。Groovy が動くサーバで、SSH ログインができる環境であればどこでも構いません。

### Production environment

* 開発環境と同じく、さくらVPSのサーバを利用しています。

### デプロイ方法

[CloudBees の Jenkins(DEV@cloud)](https://yusukei.ci.cloudbees.com/) から、ビルドジョブ及びデプロイジョブを実行することで自動的にデプロイされます。

また、Backlog の Git リポジトリに Push すると、CloudBees の Jenkins へ WebHook され、自動でビルドと開発環境へのデプロイが実行されます。

### Jenkins ジョブ一覧

| Job Name                             | 概要                                                                                                                                                                    |
|--------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| house-elf-scripts.master.build       | master ブランチからコードを取得し、テスト・ビルドを実行します。master ブランチへの push を監視し、push された際はこのジョブが自動実行されます。                         |
| house-elf-scripts.master.deploy      | house-elf-scripts.master.build の下流ビルドとして実行され、ビルド成果物を開発環境へデプロイします。                                                                     | 
| house-elf-scripts.release            | 本番環境へのリリースを行うために、本番リリースフローを実行します。master ブランチからのテスト・ビルドを実行し、問題なければ Maven リポジトリへの jar アップロードと、Git リポジトリにタグを打ちます。バージョン番号は自動でインクリメントされます。|
| house-elf-scripts.tag.build          | Git リポジトリに存在するタグを指定して、指定されたタグからビルドを行います。本番リリースフローが成功したら、このジョブを用いて本番リリース用のビルド成果物を作成します。|
| house-elf-scripts.tag.deploy         | house-elf-scripts.tag.build の下流ビルドとして実行され、ビルド成果物を本番環境へデプロイします。                                                                        |
| house-elf-scripts.reminder.execution | 本番環境へ SSH でログインし、プロジェクトディレクトリの bin/reminder スクリプトを実行します。cron ジョブとして定期実行されます。                                        |

## Contributing changes

* バグ報告は、Backlog の課題にて報告をお願いします。
* ご自身でコード修正していただき、プルリクエストも大歓迎です。

## License

TBD

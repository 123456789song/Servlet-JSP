# Git 学习笔记

## 1. Git 命令
* 1.1 初始化一个Git仓库
    * 在当前目录新建一个Git仓库
        ```
        git init
        ```
    * 将一个目录初始化为Git仓库
        ```
        git init [directory-name]
        ```
* 1.2 配置Git仓库
    * 显示当前的Git配置
        ```
        git config --list
        ```
    * 设置提交代码时的用户信息
        ```
        git config [--global] user.name "[name]"
        git config [--global] user.email "[email address]"
        ```
* 1.3 增加 / 删除暂存区的文件
    * 添加指定文件 / 目录到暂存区
        ```
        git add [file1] [file2]
        ```
    * 添加当前目录的所有文件到暂存区
        ```
        git add .
        ```
    
* 1.4 代码提交
    * 把暂存区提交到仓库区
        ```
        git commit -m [message]
        ```
    * 提交暂存区的指定文件到仓库区
        ```
        git commit [file1] [file2] ... -m [message]
        ```
    * 更改文件名字，并且将这个改名放入暂存区
        ```
        git mv [file-original] [file-renamed]
        ```
    * 更改最近一次的提交信息
        ```
        git commit --amend
        ```
    * 更改任意一次的提交信息
        ```
        git rebase -i lastcommit
        lastcommit : 要修改信息的commit的父亲commit
        ```
    * 恢复暂存区跟HEAD一样
        ```
        git reset HEAD
        ```
    * 恢复工作区跟暂存区一样
        ```
        git checkout -- [filename]
        ```
    * 重置当前分支的HEAD为指定commit，同时重置暂存区和工作区，与指定commit一致
        ```
        git reset --hard [commit]
        ```
    * 删除工作区文件，并且将这次删除放入暂存区
        ```
        git rm [file1] [file2] ...
        ```
    * 暂时将未提交的变化移除，稍后再移入
        ```
        git stash //移入
        git stash apply/pop  //弹出
        ```
* 1.5 查看信息
    * git log 常用参数
        * --oneline ：一次commit用一行显示
        * --all ：显示所有分支
        * --n [ number ] : 显示number次commit
        * --graph ：图形化分支结构的关系
    * 显示暂存区和工作区的代码差异
        ```
        git diff
        ```
    * 显示暂存区和上一个commit的差异
        ```
        git diff --cached [file]
        ```
* 1.6 分支
    * 新建一个分支，但依然停留在当前分支
    ```
    git branch [branch-name]
    ```
    * 新建一个分支，并切换到该分支
    ```
    git checkout -b [branch]
    ```
    * 删除分支
    ```
    git branch -d/-D [branch-name]
    ```

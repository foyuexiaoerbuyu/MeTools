# MeTools

java工具类

[![](https://jitpack.io/v/foyuexiaoerbuyu/MeTools.svg)](https://jitpack.io/#foyuexiaoerbuyu/MeTools)


https://jitpack.io/#foyuexiaoerbuyu/MeTools
https://github.com/foyuexiaoerbuyu/MeTools

To get a Git project into your build:

maven

Step 1. Add the JitPack repository to your build file

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	
Step 2. Add the dependency

	<dependency>
	    <groupId>com.github.foyuexiaoerbuyu</groupId>
	    <artifactId>MeTools</artifactId>
	    <version>0.06</version>
	</dependency>
 
gradle

Step 1. Add it in your root build.gradle at the end of repositories:

	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.foyuexiaoerbuyu:MeTools:Tag'
	}
Share this release:


INSERT INTO crawl_source (source_name, crawl_rule, source_status, create_time, update_time) VALUES ('书趣阁（2026）', '{
    "bookListUrl": "http://wap.xshuquge.net/sort/{catId}/0_{page}.html",
    "catIdRule": {
        "catId1": "1",
        "catId2": "2",
        "catId3": "3",
        "catId4": "4",
        "catId5": "7",
        "catId6": "6",
        "catId7": "8"
    },
    "bookIdPatten": "href=\\"/s/(\\\\d+)\\\\.html\\"",
    "pagePatten": "第(\\\\d+)/\\\\d+页",
    "totalPagePatten": "第\\\\d+/(\\\\d+)页",
    "bookDetailUrl": "http://wap.xshuquge.net/s/{bookId}.html",
    "bookNamePatten": "<a\\\\s+href=\\"/s/\\\\d+\\\\.html\\"><h2>([^/]+)</h2></a>",
    "authorNamePatten": "<p>作者：([^/]+)</p>",
    "picUrlPatten": "src=\\"(http://www.xshuquge.net/files/article/image/\\\\d+/\\\\d+/\\\\d+s\\\\.jpg)\\"",
    "statusPatten": "<p>状态：([^/]+)</p>",
    "bookStatusRule": {
        "连载中": 0,
        "完本": 1
    },
    "descStart": "<div class=\\"intro_info\\">",
    "descEnd": "最新章节推荐地址",
    "filterDesc": "",
    "bookIndexUrl": "http://www.xshuquge.net/txt/{bookId}/index.html",
    "bookIndexStart": "<dt>《",
    "indexIdPatten": "<dd><a\\\\s+href=\\"(\\\\d+)\\\\.html\\">[^/]+</a></dd>",
    "indexNamePatten": "<dd><a\\\\s+href=\\"\\\\d+\\\\.html\\">([^/]+)</a></dd>",
    "bookContentUrl": "http://www.xshuquge.net/txt/{bookId}/{indexId}.html",
    "contentStart": "<div id=\\"content\\" class=\\"showtxt\\">",
    "contentEnd": "http://www.xshuquge.net",
    "filterContent": "<b>最新网址：www.xshuquge.net</b>"
}', 1, '2020-05-18 12:02:34', '2026-10-15 17:00:00');
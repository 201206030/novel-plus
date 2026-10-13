INSERT INTO crawl_source (source_name, crawl_rule, source_status, create_time, update_time)
VALUES ('香书小说网（2026）', '{
  "bookListUrl": "http://www.ibiqugu.net/fenlei/{catId}_{page}.html",
  "catIdRule": {
    "catId1": "1",
    "catId2": "2",
    "catId3": "3",
    "catId4": "4",
    "catId5": "6",
    "catId6": "5"
  },
  "bookIdPatten": "<a\\\\s+href=\\"http://www.ibiqugu.net/(\\\\d+/\\\\d+)/\\"\\\\s+target=\\"_blank\\">",
  "pagePatten": "<em\\\\s+id=\\"pagestats\\">(\\\\d+)/\\\\d+</em>",
  "totalPagePatten": "<em\\\\s+id=\\"pagestats\\">\\\\d+/(\\\\d+)</em>",
  "bookDetailUrl": "http://www.ibiqugu.net/{bookId}/",
  "bookNamePatten": "<h1>([^/]+)</h1>",
  "authorNamePatten": "者：([^/]+)</p>",
  "picUrlPatten": "src=\\"(http://www.ibiqugu.net/files/article/image/\\\\d+/\\\\d+/\\\\d+s\\\\.jpg)\\"",
  "bookStatusRule": {},
  "descStart": "<div id=\\"intro\\">",
  "descEnd": "</div>",
  "upadateTimePatten": "<p>最后更新：(\\\\d+-\\\\d+-\\\\d+\\\\s\\\\d+:\\\\d+:\\\\d+)</p>",
  "upadateTimeFormatPatten": "yyyy-MM-dd HH:mm:ss",
  "bookIndexUrl": "http://www.ibiqugu.net/{bookId}/",
  "indexIdPatten": "<a\\\\s+href=''/\\\\d+/\\\\d+/(\\\\d+)\\\\.html''\\\\s+>[^/]+</a>",
  "indexNamePatten": "<a\\\\s+href=''/\\\\d+/\\\\d+/\\\\d+\\\\.html''\\\\s+>([^/]+)</a>",
  "bookContentUrl": "http://www.ibiqugu.net/{bookId}/{indexId}.html",
  "contentStart": "<div id=\\"content\\">",
  "contentEnd": "<p>",
  "filterContent":"<div\\\\s+id=\\"content_tip\\">\\\\s*<b>([^/]+)</b>\\\\s*</div>"
}', 0, '2026-10-13 19:22:21', '2026-10-13 19:22:21');


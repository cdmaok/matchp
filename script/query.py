#coding=utf-8

# this file is to add index by certain file.

import urllib2,os,urllib,sys

url = 'http://localhost:8080'
api = '/matchp-web/api/query?q='


def query_weibo(text):
	#data = urllib.urlencode(text)
	url_path = url+api+text
	req = urllib2.Request(url_path)
	try:
		res = urllib2.urlopen(req)
		data = res.read()
		print data
	except urllib2.HTTPError as e:
		print e.code
		print e.read()


if __name__ == '__main__':
	while True:
		text = raw_input("input your query: ")
		query_weibo(text)
	


import urllib3
import json

def new_department(name):
	http = urllib3.PoolManager()
	request = 'http://localhost:5000/api/department/create'
	response = http.request(
	    'POST',
	    request,
	    fields={'name':name})
	print(json.loads(response.data.decode('utf-8')))

def departments():
	http = urllib3.PoolManager()
	request = 'http://localhost:5000/api/department/get'
	response = http.request('GET', request)
	print(json.dumps(json.loads(response.data.decode('utf-8')),indent=2))

def get_user():
	http = urllib3.PoolManager()
	name = input('name: ')
	request = 'http://localhost:5000/api/user/' + name + '/profile'
	response = http.request('GET', request)
	print(json.loads(response.data.decode('utf-8')))

def new_user():
	http = urllib3.PoolManager()
	name = input('name: ')
	surname = input('surname: ')
	email = input('email: ')
	about = input('about: ')

	data = {'nickname':name, 'fullname':surname, 'email':email, 'about':about}
	encoded_data = json.dumps(data).encode('utf-8')
	request = 'http://localhost:5000/api/user/' + name + '/create'
	response = http.request(
	    'POST',
	    request,
	    body=encoded_data,
	    headers={'Content-Type': 'application/json'})
	print(json.loads(response.data.decode('utf-8')))

def search_book():
	http = urllib3.PoolManager()
	action = input('enter \'a\' to search by autor or \'t\' to search by title ')
	if(action=='a'):
		author = input('enter author name: ')
		request = 'http://localhost:5000/api/book/get?author=' + author
	elif(action=='t'):
		title = input('enter book title: ')
		request = 'http://localhost:5000/api/book/get?title=' + title
	else:
		return
	response = http.request('GET', request)
	print(json.dumps(json.loads(response.data.decode('utf-8')),indent=2))

def add_book():
	http = urllib3.PoolManager()
	author = input('author: ')
	publisher = input('publisher: ')
	title = input('title: ')
	department = input('department: ')

	data = {'author':author, 'publisher':publisher, 'title':title, 'department':department}
	encoded_data = json.dumps(data).encode('utf-8')
	request = 'http://localhost:5000/api/book/create'
	response = http.request(
	    'POST',
	    request,
	    body=encoded_data,
	    headers={'Content-Type': 'application/json'})
	print(json.loads(response.data.decode('utf-8')))

def assign_book():
	uid = input('user id: ')
	bid = input('book_id: ')





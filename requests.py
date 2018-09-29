import urllib3
import json

def new_department(name):
	http = urllib3.PoolManager()
	request = 'http://localhost:5000/api/department/create'
	response = http.request(
	    'POST',
	    request,
	    fields={'name':name})
	#print(json.loads(response.data.decode('utf-8')))
	parse_short_json(json.loads(response.data.decode('utf-8')))

def departments():
	http = urllib3.PoolManager()
	request = 'http://localhost:5000/api/department/get'
	response = http.request('GET', request)
	#print(json.dumps(json.loads(response.data.decode('utf-8')),indent=2))
	parse_json(json.loads(response.data.decode('utf-8')))

def dept_all():
	http = urllib3.PoolManager()
	dept = input('department: ')
	request = 'http://localhost:5000/api/book/' + dept + '/get'
	response = http.request('GET', request)
	#print(json.dumps(json.loads(response.data.decode('utf-8')),indent=2))
	parse_json(json.loads(response.data.decode('utf-8')))

def get_user():
	http = urllib3.PoolManager()
	email = input('email: ')
	request = 'http://localhost:5000/api/user/' + email + '/profile'
	response = http.request('GET', request)
	#print(json.loads(response.data.decode('utf-8')))
	parse_short_json(json.loads(response.data.decode('utf-8')))

def new_user(name, surname, email, age):
	http = urllib3.PoolManager()

	data = {'nickname':name, 'fullname':surname, 'email':email, 'age':age}
	encoded_data = json.dumps(data).encode('utf-8')
	request = 'http://localhost:5000/api/user/' + email + '/create'
	response = http.request(
	    'POST',
	    request,
	    body=encoded_data,
	    headers={'Content-Type': 'application/json'})
	print(json.loads(response.data.decode('utf-8')))
	#parse_short_json(json.loads(response.data.decode('utf-8'))[0])

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
	#print(json.dumps(json.loads(response.data.decode('utf-8')),indent=2))
	parse_json(json.loads(response.data.decode('utf-8')))

def add_book(author, title, publisher, department):
	http = urllib3.PoolManager()
	data = {'author':author, 'publisher':publisher, 'title':title, 'department':department}
	encoded_data = json.dumps(data).encode('utf-8')
	request = 'http://localhost:5000/api/book/create'
	response = http.request(
	    'POST',
	    request,
	    body=encoded_data,
	    headers={'Content-Type': 'application/json'})
	#print(json.loads(response.data.decode('utf-8')))
	parse_short_json(json.loads(response.data.decode('utf-8')))

def assign_book(uid, bid):
	http = urllib3.PoolManager()
	request = 'http://localhost:5000/api/assign?uid=' + uid + '&bid=' + bid
	response = http.request('POST',request)
	#print(json.loads(response.data.decode('utf-8')))
	parse_short_json(json.loads(response.data.decode('utf-8')))

def return_book(uid,bid):
	http = urllib3.PoolManager()
	request = 'http://localhost:5000/api/return?uid=' + uid + '&bid=' + bid
	response = http.request('POST',request)
	#print(json.loads(response.data.decode('utf-8')))
	parse_short_json(json.loads(response.data.decode('utf-8')))

def user_assigned():
	http = urllib3.PoolManager()
	id = input('user id: ')
	request = 'http://localhost:5000/api/user/' + id + '/get'
	response = http.request('GET', request)
	#print(json.dumps(json.loads(response.data.decode('utf-8')),indent=2))
	parse_json(json.loads(response.data.decode('utf-8')))

def top_assigned():
	http = urllib3.PoolManager()
	number = input('number of books in top: ')
	request = 'http://localhost:5000/api/book/top/' + number + '/get'
	response = http.request('GET', request)
	#print(json.dumps(json.loads(response.data.decode('utf-8')),indent=2))
	parse_json(json.loads(response.data.decode('utf-8')))
	

def fill_users():
	new_user('daria', 'gorokhova', 'dariago12@gmail.com', 20)
	new_user('vasya', 'pupkin', 'vasya@gmail.com', 15)
	new_user('ivan', 'ivanov', 'ii@mail.ru', 45)
	new_user('olga', 'nikolaeva', 'on1994@gmail.com', 24)
	new_user('olga', 'nikolaeva', 'olga@mail.ru', 31)

def fill_books():
	add_book('Pushkin', 'Ruslan_i_Ludmila', 'classiclit', 'classic')
	add_book('Pushkin', 'Evgeniy_Onegin', 'classiclit', 'classic')
	add_book('Pushkin', 'Evgeniy_Onegin', 'classiclit', 'classic')
	add_book('Pushkin', 'Evgeniy_Onegin', 'classiclit', 'classic')
	add_book('Pushkin', 'Boris_Godunov', 'classiclit', 'classic')
	add_book('Shakespeare', 'Hamlet', 'worldclassic', 'classic')
	add_book('Shakespeare', 'Korol_Lir', 'penguin', 'classic')

	add_book('Tolstaya', 'short_stories', 'eksmo', 'modern')
	add_book('Pelevin', 'Generation_P', 'eksmo', 'modern')
	add_book('Tolstaya', 'short_stories', 'eksmo', 'modern')
	add_book('Orwell', '1984', 'worldbooks', 'modern')
	
	add_book('Shakespeare', 'King_Lear', 'penguin', 'foreign')
	add_book('Cunningham', 'Specimen_Days', 'penguin', 'foreign')
	add_book('Cunningham', 'Hours', 'penguin', 'foreign')
	add_book('Whitman', 'Specimen_Days', 'penguin', 'foreign')

	add_book('Tanenbaum', 'Sovremennye_operatsionnye_sistemy', 'compbooks', 'science')
	add_book('Tanenbaum', 'Kompyternye_seti', 'compbooks', 'science')

def fill_assign_and_return():
	assign_book('1','16')
	assign_book('1','17')
	assign_book('1','4')
	assign_book('1','3')
	assign_book('1','5')
	return_book('1','16')
	return_book('1','3')
	return_book('1','5')

	assign_book('2','16')
	assign_book('2','15')
	return_book('2','16')
	return_book('2','15')

	assign_book('3','16')
	assign_book('3','3')
	assign_book('3','8')
	return_book('3','8')
	return_book('3','3')

def parse_json(input):
	keys = list(input[0].keys())
	header = ""
	for i in range(len(keys)):
		header = header + "                       " + keys[i]
	print(header+'\n')

	for i in range(len(input)):
		for j in range(len(keys)):	
			print(str(input[i].get(keys[j])).rjust(30),end='')	
		print()

def parse_short_json(input):
	keys = list(input.keys())
	for i in range(len(keys)-1):
		print(keys[i], ': ', input.get(keys[i]), ', ', end='')
	print(keys[len(keys)-1], ': ', input.get(keys[len(keys)-1]))






from requests import *

new_department('classic')
new_department('modern')
new_department('science')
new_department('foreign')

fill_users()
fill_books()
fill_assign_and_return()

while(1):
	print('\nMAIN MENU\n u - user\n b - books\n d - departments\n t - top\n q - exit')
	action = input()

	if(action=='u'):
		print('\nUSER MENU\n 0 - back to main menu\n 1 - create\n 2 - show profile\n 3 - list of books assigned to user')
		action2 = input()
		if(action2=='0'):
			continue
		if(action2=='1'):
			name = input('name: ')
			surname = input('surname: ')
			email = input('email: ')
			age = input('age: ')
			new_user(name, surname, email, age)
		if(action2=='2'):
			get_user()
		if(action2=='3'):
			user_assigned()

	if(action=='b'):
		print('\nBOOK MENU\n 0 - back to main menu\n 1 - add book to library\n 2 - search for book\n 3 - assign book to user\n 4 - return assigned book')
		action2 = input()
		if(action2=='0'):
			continue
		if(action2=='1'):
			author = input('author: ')
			title = input('title: ')
			publisher = input('publisher: ')
			department = input('department: ')
			add_book(author, title, publisher, department)
		if(action2=='2'):
			search_book()
		if(action2=='3'):
			uid = input('user id: ')
			bid = input('book id: ')
			assign_book(uid, bid)
		if(action2=='4'):
			uid = input('user id: ')
			bid = input('book id: ')
			return_book(uid,bid)

	if(action=='d'):
		print('\nDEPARTMENT MENU\n 0 - back to main menu\n 1 - show all departments\n 2 - show list of books in department')
		action2 = input()
		if(action2=='0'):
			continue
		if(action2=='1'):
			departments()
		if(action2=='2'):
			dept_all()

	if(action=='q'):
		exit()

	if(action=='t'):
		top_assigned()


from requests import *

new_department('classic')
new_department('modern')
new_department('science')
new_department('foreign')

while(1):
	print('\nMAIN MENU\n u - user\n b - books\n d - departments\n q - exit')
	action = input()

	if(action=='u'):
		print('\nUSER MENU\n 0 - back to main menu\n 1 - create\n 2 - show profile')
		action2 = input()
		if(action2=='0'):
			continue
		if(action2=='1'):
			new_user()
		if(action2=='2'):
			get_user()

	if(action=='b'):
		print('\nBOOK MENU\n 0 - back to main menu\n 1 - add book to library\n 2 - search for book\n 3 - assign book to user')
		action2 = input()
		if(action2=='0'):
			continue
		if(action2=='1'):
			add_book()
		if(action2=='2'):
			search_book()
		if(action2=='3'):
			assign_book()

	if(action=='d'):
		print('\nDEPARTMENT MENU\n 0 - back to main menu\n 1 - show all departments\n 2 - show list of books in department')
		action2 = input()
		if(action2=='0'):
			continue
		if(action2=='1'):
			departments()

	if(action=='q'):
		exit()


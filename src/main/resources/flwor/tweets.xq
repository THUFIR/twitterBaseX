for $tweets  in db:open("twitter")
return data($tweets/json/id__str)


TODO:

- Add ElasticSearch index
- Add endpoint to (re-)index the annotations in the postgres db
- Add endpoint to find annotations using ElasticSearch queries
- Return a detailed 400 BAD REQUEST response when uploading annotations with @context-less custom fields (which are currently silently stripped from the annotation)
- Add endpoint for uploading annotations in batch
- Add option to connect to provenance server; send provenance records when appropriate

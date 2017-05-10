graphana endpoint: http://localhost:3000

When the dashboard shows up go to dashboard import and import the json file under docker/ghibli/graphana/dashboards.

or run teh following command pointing to the dashboard json file:

curl -v POST http://admin:admin@localhost:3000/api/dashboards/import -d @ghibli-activemq-DLQ.json --header "Content-Type: application/json"

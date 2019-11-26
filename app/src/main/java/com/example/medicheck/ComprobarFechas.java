



/*class Avisar extends AsyncTask<String, Integer, String> {




    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task
    }

    @Override
    protected String doInBackground(String... params) {
        //Do some task
        publishProgress(1);
        Avisos aviso = AvisosRepository.comprobarFecha();
        if (aviso != null) {
            ComprobarFechas cp = new ComprobarFechas();
            cp.notificacion(context, aviso);
        } else {
            //3600 *
            int time = 1000;
            try {
                Thread.sleep(time);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //Update the progress of current task

    }

    @Override
    protected void onPostExecute(String s) {
        //Show the result obtained from doInBackground
        Avisar objAvisar = new Avisar();
        objAvisar.execute();
    }
}*/

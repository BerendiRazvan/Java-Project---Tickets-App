using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MPP_Proiect.domain
{
    class Bilet
    {
        private string numeCumparator;
        private int locuri;

        public Bilet(string numeCumparator, int locuri)
        {
            this.numeCumparator = numeCumparator;
            this.locuri = locuri;
        }

        public string getNumeCumparator()
        {
            return numeCumparator;
        }

        public void setNumeCumparator(string numeCumparator)
        {
            this.numeCumparator = numeCumparator;
        }

        public int getLocuri()
        {
            return locuri;
        }

        public void setLocuri(int locuri)
        {
            this.locuri = locuri;
        }

        public override string ToString()
        {
            return "Bilet{" +
                "numeCumparator='" + numeCumparator + '\'' +
                ", locuri=" + locuri +
                '}';
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MPP_Proiect.domain
{
    class Locatie
    {
        private string tara;
        private string oras;

        public Locatie(string tara, string oras)
        {
            this.tara = tara;
            this.oras = oras;
        }

        public string getTara()
        {
            return tara;
        }

        public void setTara(string tara)
        {
            this.tara = tara;
        }

        public string getOras()
        {
            return oras;
        }

        public void setOras(string oras)
        {
            this.oras = oras;
        }

        public override string ToString()
        {
            return "Locatie{" +
                "Tara='" + tara + '\'' +
                ", Oras='" + oras + '\'' +
                '}';
        }
    }
}

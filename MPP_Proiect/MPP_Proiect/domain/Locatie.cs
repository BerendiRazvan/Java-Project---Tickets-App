using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MPP_Proiect.domain
{
    class Locatie
    {
        private string tara { get; set; }
        private string oras { get; set; }

        public Locatie(string tara, string oras)
        {
            this.tara = tara;
            this.oras = oras;
        }


        public override string ToString()
        {
            return "Locatie{" +
                "tara='" + tara + '\'' +
                ", oras='" + oras + '\'' +
                '}';
        }
    }
}

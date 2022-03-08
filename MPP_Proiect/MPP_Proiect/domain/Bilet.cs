using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MPP_Proiect.domain
{
    class Bilet
    {
        private string numeCumparator { get; set; }
        private int locuri { get; set; }
        private Spectacol spectacol { get; set; }

        public Bilet(string numeCumparator, int locuri)
        {
            this.numeCumparator = numeCumparator;
            this.locuri = locuri;
        }

        public override string ToString()
        {
            return "Bilet{" +
                "numeCumparator='" + numeCumparator + '\'' +
                ", locuri=" + locuri +
                ", spectacol=" + spectacol +
                '}';
        }
    }
}

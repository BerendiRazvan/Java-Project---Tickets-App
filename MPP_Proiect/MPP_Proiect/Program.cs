using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using MPP_Proiect.domain;


namespace MPP_Proiect
{
    class Program
    {
        static void Main(string[] args)
        {
            DateTime time = new DateTime(2021,10,2,10,30,0);
            Spectacol a = new Spectacol("Smiley", time, new Locatie("Romania", "Cluj"), 100, 1234);

            a.id = 221;

            Console.WriteLine(a);
        }
    }
}
